package com.flowboard.label.repository;

import com.flowboard.label.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository
        extends JpaRepository<Label, Integer> {

    List<Label> findByBoardId(int boardId);

    Optional<Label> findByLabelId(int labelId);
}