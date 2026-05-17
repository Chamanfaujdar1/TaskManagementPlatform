package com.flowboard.card.service;

import com.flowboard.card.dto.CardDto;
import java.util.List;

public interface CardService {

    CardDto createCard(CardDto cardDto);

    CardDto getCardById(int cardId);

    List<CardDto> getCardsByList(int listId);

    List<CardDto> getCardsByBoard(int boardId);

    List<CardDto> getCardsByAssignee(int assigneeId);

    CardDto updateCard(int cardId, CardDto cardDto);

    CardDto moveCard(int cardId, int targetListId,
                  int newPosition);

    void reorderCards(int listId,
                      List<Integer> orderedCardIds);

    void archiveCard(int cardId);

    void unarchiveCard(int cardId);

    void deleteCard(int cardId);

    CardDto setAssignee(int cardId, Integer assigneeId);

    CardDto setPriority(int cardId, String priority);

    CardDto setStatus(int cardId, String status);

    List<CardDto> getOverdueCards();

    List<CardDto> searchCards(String query);

    List<CardDto> getAllCards();
}