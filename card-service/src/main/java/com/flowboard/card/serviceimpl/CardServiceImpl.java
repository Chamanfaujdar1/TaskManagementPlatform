package com.flowboard.card.serviceimpl;

import com.flowboard.card.entity.Card;
import com.flowboard.card.model.CardDocument;
import com.flowboard.card.exception.BadRequestException;
import com.flowboard.card.exception.ResourceNotFoundException;
import com.flowboard.card.repository.CardRepository;
import com.flowboard.card.repository.CardSearchRepository;
import com.flowboard.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired(required = false)
    private CardSearchRepository cardSearchRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired(required = false)
    private org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private void broadcastUpdate(String type, Integer boardId, Object payload) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("type", type);
            request.put("boardId", boardId);
            request.put("payload", payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            restTemplate.postForEntity(notificationServiceUrl + "/api/v1/notifications/broadcast", entity, String.class);
        } catch (Exception e) {
            System.err.println("Failed to broadcast real-time update: " + e.getMessage());
        }
    }

    @Override
    public Card createCard(Card card) {
        if (card.getTitle() == null || card.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Card title cannot be empty");
        }
        long count = cardRepository.countByListId(card.getListId());
        card.setPosition((int) count);
        card.setIsArchived(false);
        Card saved = cardRepository.save(card);
        
        // Trigger notification if assignee is set
        if (saved.getAssigneeId() != null) {
            sendAssignmentNotification(saved);
        }

        broadcastUpdate("CARD_CREATED", saved.getBoardId(), saved);
        syncToElasticsearch(saved);
        return saved;
    }

    private void syncToElasticsearch(Card card) {
        try {
            if (cardSearchRepository == null) return;
            CardDocument doc = new CardDocument(
                    card.getCardId(), card.getTitle(), card.getDescription(),
                    card.getBoardId(), card.getListId(), card.getPriority(),
                    card.getStatus(), card.getAssigneeId(), card.getIsArchived()
            );
            cardSearchRepository.save(doc);
        } catch (Exception e) {
            System.err.println("Failed to sync card to Elasticsearch: " + e.getMessage());
        }
    }

    @Override
    public Card getCardById(int cardId) {
        return cardRepository.findByCardId(cardId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card not found with id: " + cardId));
    }

    @Override
    public List<Card> getCardsByList(int listId) {
        return cardRepository.findByListIdOrderByPosition(listId);
    }

    @Override
    public List<Card> getCardsByBoard(int boardId) {
        return cardRepository.findByBoardId(boardId);
    }

    @Override
    public List<Card> getCardsByAssignee(int assigneeId) {
        return cardRepository.findByAssigneeId(assigneeId);
    }

    @Override
    public Card updateCard(int cardId, Card updated) {
        Card existing = getCardById(cardId);
        if (existing.getIsArchived()) {
            throw new BadRequestException("Cannot update an archived card with id: " + cardId);
        }
        if (updated.getTitle() != null) existing.setTitle(updated.getTitle());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getPriority() != null) existing.setPriority(updated.getPriority());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getDueDate() != null) existing.setDueDate(updated.getDueDate());
        if (updated.getStartDate() != null) existing.setStartDate(updated.getStartDate());
        if (updated.getCoverColor() != null) existing.setCoverColor(updated.getCoverColor());
        
        // Handle assignee (allow null for Unassigned)
        Integer oldAssigneeId = existing.getAssigneeId();
        Integer newAssigneeId = updated.getAssigneeId();
        System.out.println("Updating card assignee: " + oldAssigneeId + " -> " + newAssigneeId);
        
        existing.setAssigneeId(newAssigneeId);
        Card saved = cardRepository.save(existing);
        
        // Trigger notification if assignee changed and is not null
        if (saved.getAssigneeId() != null && !saved.getAssigneeId().equals(oldAssigneeId)) {
            System.out.println("Assignee changed! Triggering RabbitMQ notification...");
            sendAssignmentNotification(saved);
        } else {
            System.out.println("Assignee didn't change or is null. No notification sent.");
        }

        broadcastUpdate("CARD_UPDATED", saved.getBoardId(), saved);
        syncToElasticsearch(saved);
        return saved;
    }

    private void sendAssignmentNotification(Card card) {
        try {
            com.flowboard.card.model.NotificationEvent event = com.flowboard.card.model.NotificationEvent.builder()
                    .recipientId(card.getAssigneeId())
                    .actorId(0) // System or current user
                    .type("ASSIGNMENT")
                    .title("New Card Assigned")
                    .message("You have been assigned to: " + card.getTitle())
                    .build();
            
            System.out.println("Attempting to send RabbitMQ notification for card: " + card.getTitle() + " to user: " + card.getAssigneeId());
            if (rabbitTemplate == null) {
                throw new RuntimeException("RabbitTemplate is not available");
            }
            rabbitTemplate.convertAndSend(
                com.flowboard.card.config.RabbitMQConfig.EXCHANGE,
                com.flowboard.card.config.RabbitMQConfig.ROUTING_KEY,
                event
            );
            System.out.println("RabbitMQ notification event sent successfully.");
        } catch (Exception e) {
            System.err.println("RabbitMQ failed, attempting REST fallback: " + e.getMessage());
            try {
                Map<String, Object> notification = new HashMap<>();
                notification.put("recipientId", card.getAssigneeId());
                notification.put("actorId", 0);
                notification.put("type", "ASSIGNMENT");
                notification.put("title", "New Card Assigned");
                notification.put("message", "You have been assigned to: " + card.getTitle());
                notification.put("isRead", false);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(notification, headers);
                
                restTemplate.postForEntity(notificationServiceUrl + "/api/v1/notifications", entity, String.class);
                System.out.println("REST fallback notification sent successfully.");
            } catch (Exception re) {
                System.err.println("CRITICAL: Both RabbitMQ and REST fallback failed: " + re.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public Card moveCard(int cardId, int targetListId, int newPosition) {
        Card card = getCardById(cardId);

        List<Card> sourceCards = cardRepository.findByListIdOrderByPosition(card.getListId());
        sourceCards.remove(card);
        for (int i = 0; i < sourceCards.size(); i++) {
            sourceCards.get(i).setPosition(i);
            cardRepository.save(sourceCards.get(i));
        }

        List<Card> targetCards = cardRepository.findByListIdOrderByPosition(targetListId);
        for (Card c : targetCards) {
            if (c.getPosition() >= newPosition) {
                c.setPosition(c.getPosition() + 1);
                cardRepository.save(c);
            }
        }

        card.setListId(targetListId);
        card.setPosition(newPosition);
        Card saved = cardRepository.save(card);
        broadcastUpdate("CARD_MOVED", saved.getBoardId(), saved);
        syncToElasticsearch(saved);
        return saved;
    }

    @Override
    @Transactional
    public void reorderCards(int listId, List<Integer> orderedCardIds) {
        if (orderedCardIds == null || orderedCardIds.isEmpty()) {
            throw new BadRequestException("Ordered card IDs cannot be empty");
        }
        List<Card> cards = cardRepository.findByListIdOrderByPosition(listId);
        for (int i = 0; i < orderedCardIds.size(); i++) {
            final int newPosition = i;
            final int cardId = orderedCardIds.get(i);
            cards.stream()
                    .filter(c -> c.getCardId().equals(cardId))
                    .findFirst()
                    .ifPresent(c -> {
                        c.setPosition(newPosition);
                        cardRepository.save(c);
                    });
        }
    }

    @Override
    public void archiveCard(int cardId) {
        Card card = getCardById(cardId);
        if (card.getIsArchived()) {
            throw new BadRequestException("Card is already archived");
        }
        card.setIsArchived(true);
        Card saved = cardRepository.save(card);
        broadcastUpdate("CARD_ARCHIVED", saved.getBoardId(), saved);
        syncToElasticsearch(saved);
    }

    @Override
    public void unarchiveCard(int cardId) {
        Card card = getCardById(cardId);
        if (!card.getIsArchived()) {
            throw new BadRequestException("Card is not archived");
        }
        card.setIsArchived(false);
        Card saved = cardRepository.save(card);
        broadcastUpdate("CARD_UNARCHIVED", saved.getBoardId(), saved);
        syncToElasticsearch(saved);
    }

    @Override
    @Transactional
    public void deleteCard(int cardId) {
        getCardById(cardId);
        cardRepository.deleteById(cardId);
        try {
            if (cardSearchRepository != null) {
                cardSearchRepository.deleteById(cardId);
            }
        } catch (Exception e) {
            System.err.println("Failed to delete card from Elasticsearch: " + e.getMessage());
        }
    }

    @Override
    public Card setAssignee(int cardId, Integer assigneeId) {
        Card card = getCardById(cardId);
        Integer oldAssigneeId = card.getAssigneeId();
        
        System.out.println("SetAssignee called: " + oldAssigneeId + " -> " + assigneeId);
        
        card.setAssigneeId(assigneeId);
        Card saved = cardRepository.save(card);
        
        if (saved.getAssigneeId() != null && !saved.getAssigneeId().equals(oldAssigneeId)) {
            System.out.println("Assignee changed via setAssignee! Triggering notification...");
            sendAssignmentNotification(saved);
        }
        return saved;
    }

    @Override
    public Card setPriority(int cardId, String priority) {
        Card card = getCardById(cardId);
        card.setPriority(priority);
        return cardRepository.save(card);
    }

    @Override
    public Card setStatus(int cardId, String status) {
        Card card = getCardById(cardId);
        card.setStatus(status);
        Card saved = cardRepository.save(card);
        if (cardSearchRepository != null) {
            syncToElasticsearch(saved);
        }
        return saved;
    }

    @Override
    public List<Card> searchCards(String query) {
        return cardRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public List<Card> getOverdueCards() {
        return cardRepository.findByDueDateBeforeAndStatusNot(LocalDate.now(), "DONE");
    }
}