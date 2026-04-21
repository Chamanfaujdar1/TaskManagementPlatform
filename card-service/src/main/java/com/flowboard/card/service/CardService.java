package com.flowboard.card.service;

import com.flowboard.card.entity.Card;
import java.util.List;

public interface CardService {

    Card createCard(Card card);

    Card getCardById(int cardId);

    List<Card> getCardsByList(int listId);

    List<Card> getCardsByBoard(int boardId);

    List<Card> getCardsByAssignee(int assigneeId);

    Card updateCard(int cardId, Card card);

    Card moveCard(int cardId, int targetListId,
                  int newPosition);

    void reorderCards(int listId,
                      List<Integer> orderedCardIds);

    void archiveCard(int cardId);

    void unarchiveCard(int cardId);

    void deleteCard(int cardId);

    Card setAssignee(int cardId, int assigneeId);

    Card setPriority(int cardId, String priority);

    Card setStatus(int cardId, String status);

    List<Card> getOverdueCards();
}