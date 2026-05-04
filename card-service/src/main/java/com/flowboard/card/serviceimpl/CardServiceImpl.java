package com.flowboard.card.serviceimpl;

import com.flowboard.card.entity.Card;
import com.flowboard.card.exception.BadRequestException;
import com.flowboard.card.exception.ResourceNotFoundException;
import com.flowboard.card.repository.CardRepository;
import com.flowboard.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card createCard(Card card) {
        if (card.getTitle() == null || card.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Card title cannot be empty");
        }
        long count = cardRepository.countByListId(card.getListId());
        card.setPosition((int) count);
        card.setIsArchived(false);
        return cardRepository.save(card);
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
        return cardRepository.save(existing);
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
        return cardRepository.save(card);
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
        cardRepository.save(card);
    }

    @Override
    public void unarchiveCard(int cardId) {
        Card card = getCardById(cardId);
        if (!card.getIsArchived()) {
            throw new BadRequestException("Card is not archived");
        }
        card.setIsArchived(false);
        cardRepository.save(card);
    }

    @Override
    @Transactional
    public void deleteCard(int cardId) {
        getCardById(cardId);
        cardRepository.deleteById(cardId);
    }

    @Override
    public Card setAssignee(int cardId, int assigneeId) {
        Card card = getCardById(cardId);
        card.setAssigneeId(assigneeId);
        return cardRepository.save(card);
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
        return cardRepository.save(card);
    }

    @Override
    public List<Card> getOverdueCards() {
        return cardRepository.findByDueDateBeforeAndStatusNot(LocalDate.now(), "DONE");
    }
}