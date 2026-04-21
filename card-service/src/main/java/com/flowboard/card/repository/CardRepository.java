package com.flowboard.card.repository;

import com.flowboard.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository
        extends JpaRepository<Card, Integer> {

    List<Card> findByListIdOrderByPosition(int listId);

    List<Card> findByBoardId(int boardId);

    List<Card> findByAssigneeId(int assigneeId);

    Optional<Card> findByCardId(int cardId);

    List<Card> findByPriority(String priority);

    List<Card> findByStatus(String status);

    long countByListId(int listId);

    List<Card> findByDueDateBeforeAndStatusNot(
            LocalDate date, String status);

    List<Card> findByBoardIdAndIsArchived(
            int boardId, Boolean isArchived);
}