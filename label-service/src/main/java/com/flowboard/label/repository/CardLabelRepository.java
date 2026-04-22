package com.flowboard.label.repository;

import com.flowboard.label.entity.CardLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardLabelRepository
        extends JpaRepository<CardLabel, Integer> {

    List<CardLabel> findByCardId(int cardId);

    List<CardLabel> findByLabelId(int labelId);

    boolean existsByCardIdAndLabelId(
            int cardId, int labelId);

    void deleteByCardIdAndLabelId(
            int cardId, int labelId);
}