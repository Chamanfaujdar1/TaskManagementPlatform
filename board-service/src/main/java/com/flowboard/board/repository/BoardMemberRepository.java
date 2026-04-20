package com.flowboard.board.repository;

import com.flowboard.board.entity.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardMemberRepository
        extends JpaRepository<BoardMember, Integer> {

    List<BoardMember> findByBoardId(int boardId);

    Optional<BoardMember> findByBoardIdAndUserId(
            int boardId, int userId);

    boolean existsByBoardIdAndUserId(int boardId, int userId);

    void deleteByBoardIdAndUserId(int boardId, int userId);
}