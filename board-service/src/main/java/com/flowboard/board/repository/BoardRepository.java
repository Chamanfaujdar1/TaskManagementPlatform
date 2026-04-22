package com.flowboard.board.repository;

import com.flowboard.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    List<Board> findByWorkspaceId(int workspaceId);

    Optional<Board> findByBoardId(int boardId);

    List<Board> findByCreatedById(int createdById);

    List<Board> findByVisibility(String visibility);

    List<Board> findByIsClosed(Boolean isClosed);

    long countByWorkspaceId(int workspaceId);

    @Query("SELECT b FROM Board b JOIN BoardMember bm " +
            "ON b.boardId = bm.boardId WHERE bm.userId = :userId")
    List<Board> findByMemberUserId(@Param("userId") int userId);
}