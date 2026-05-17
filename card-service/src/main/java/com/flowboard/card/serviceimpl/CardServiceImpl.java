package com.flowboard.card.serviceimpl;

import com.flowboard.card.dto.CardDto;
import com.flowboard.card.entity.Card;
import com.flowboard.card.model.CardDocument;
import com.flowboard.card.exception.BadRequestException;
import com.flowboard.card.exception.ResourceNotFoundException;
import com.flowboard.card.mapper.CardMapper;
import com.flowboard.card.repository.CardRepository;
import com.flowboard.card.repository.CardSearchRepository;
import com.flowboard.card.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    
    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private CardSearchRepository cardSearchRepository;
    
    private final RestTemplate restTemplate;
    private final org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private Card findCardById(int cardId) {
        return cardRepository.findByCardId(cardId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card not found with id: " + cardId));
    }

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
            log.error("Failed to broadcast real-time update: {}", e.getMessage());
        }
    }

    @Override
    public CardDto createCard(CardDto cardDto) {
        Card card = CardMapper.mapToEntity(cardDto);
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
        return CardMapper.mapToDto(saved);
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
            log.error("Failed to sync card to Elasticsearch: {}", e.getMessage());
        }
    }

    @Override
    public CardDto getCardById(int cardId) {
        return CardMapper.mapToDto(findCardById(cardId));
    }

    @Override
    public List<CardDto> getCardsByList(int listId) {
        return cardRepository.findByListIdOrderByPosition(listId).stream()
                .map(CardMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardDto> getCardsByBoard(int boardId) {
        return cardRepository.findByBoardId(boardId).stream()
                .map(CardMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardDto> getCardsByAssignee(int assigneeId) {
        return cardRepository.findByAssigneeId(assigneeId).stream()
                .map(CardMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CardDto updateCard(int cardId, CardDto updated) {
        Card existing = findCardById(cardId);
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
        log.info("Updating card assignee: {} -> {}", oldAssigneeId, newAssigneeId);
        
        existing.setAssigneeId(newAssigneeId);
        Card saved = cardRepository.save(existing);
        
        // Trigger notification if assignee changed and is not null
        if (saved.getAssigneeId() != null && !saved.getAssigneeId().equals(oldAssigneeId)) {
            log.info("Assignee changed! Triggering RabbitMQ notification...");
            sendAssignmentNotification(saved);
        } else {
            log.info("Assignee didn't change or is null. No notification sent.");
        }

        broadcastUpdate("CARD_UPDATED", saved.getBoardId(), saved);
        syncToElasticsearch(saved);
        return CardMapper.mapToDto(saved);
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
            
            log.info("Attempting to send RabbitMQ notification for card: {} to user: {}", card.getTitle(), card.getAssigneeId());
            if (rabbitTemplate == null) {
                throw new RuntimeException("RabbitTemplate is not available");
            }
            rabbitTemplate.convertAndSend(
                com.flowboard.card.config.RabbitMQConfig.EXCHANGE,
                com.flowboard.card.config.RabbitMQConfig.ROUTING_KEY,
                event
            );
            log.info("RabbitMQ notification event sent successfully.");
        } catch (Exception e) {
            log.error("RabbitMQ failed, attempting REST fallback: {}", e.getMessage());
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
                log.info("REST fallback notification sent successfully.");
            } catch (Exception re) {
                log.error("CRITICAL: Both RabbitMQ and REST fallback failed: {}", re.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public CardDto moveCard(int cardId, int targetListId, int newPosition) {
        Card card = findCardById(cardId);

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
        return CardMapper.mapToDto(saved);
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
        Card card = findCardById(cardId);
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
        Card card = findCardById(cardId);
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
        findCardById(cardId);
        cardRepository.deleteById(cardId);
        try {
            if (cardSearchRepository != null) {
                cardSearchRepository.deleteById(cardId);
            }
        } catch (Exception e) {
            log.error("Failed to delete card from Elasticsearch: {}", e.getMessage());
        }
    }

    @Override
    public CardDto setAssignee(int cardId, Integer assigneeId) {
        Card card = findCardById(cardId);
        Integer oldAssigneeId = card.getAssigneeId();
        
        log.info("SetAssignee called: {} -> {}", oldAssigneeId, assigneeId);
        
        card.setAssigneeId(assigneeId);
        Card saved = cardRepository.save(card);
        
        if (saved.getAssigneeId() != null && !saved.getAssigneeId().equals(oldAssigneeId)) {
            log.info("Assignee changed via setAssignee! Triggering notification...");
            sendAssignmentNotification(saved);
        }
        return CardMapper.mapToDto(saved);
    }

    @Override
    public CardDto setPriority(int cardId, String priority) {
        Card card = findCardById(cardId);
        card.setPriority(priority);
        return CardMapper.mapToDto(cardRepository.save(card));
    }

    @Override
    public CardDto setStatus(int cardId, String status) {
        Card card = findCardById(cardId);
        card.setStatus(status);
        Card saved = cardRepository.save(card);
        if (cardSearchRepository != null) {
            syncToElasticsearch(saved);
        }
        return CardMapper.mapToDto(saved);
    }

    @Override
    public List<CardDto> searchCards(String query) {
        return cardRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query).stream()
                .map(CardMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream()
                .map(CardMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardDto> getOverdueCards() {
        return cardRepository.findByDueDateBeforeAndStatusNot(LocalDate.now(), "DONE").stream()
                .map(CardMapper::mapToDto)
                .collect(Collectors.toList());
    }
}