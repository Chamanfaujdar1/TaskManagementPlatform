package com.flowboard.comment.repository;

import com.flowboard.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository
        extends JpaRepository<Comment, Integer> {

    List<Comment> findByCardId(int cardId);

    List<Comment> findByAuthorId(int authorId);

    Optional<Comment> findByCommentId(int commentId);

    List<Comment> findByParentCommentId(
            int parentCommentId);

    long countByCardId(int cardId);

    void deleteByCommentId(int commentId);
}