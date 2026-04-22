package com.flowboard.comment.serviceimpl;

import com.flowboard.comment.entity.Attachment;
import com.flowboard.comment.entity.Comment;
import com.flowboard.comment.repository.AttachmentRepository;
import com.flowboard.comment.repository.CommentRepository;
import com.flowboard.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public Comment addComment(Comment comment) {
        comment.setIsDeleted(false);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getByCard(int cardId) {
        return commentRepository.findByCardId(cardId);
    }

    @Override
    public Comment getCommentById(int commentId) {
        return commentRepository.findByCommentId(commentId)
                .orElseThrow(() ->
                        new RuntimeException("Comment not found"));
    }

    @Override
    public List<Comment> getReplies(int parentCommentId) {
        return commentRepository
                .findByParentCommentId(parentCommentId);
    }

    @Override
    public Comment updateComment(
            int commentId, String content) {
        Comment comment = getCommentById(commentId);
        if (comment.getIsDeleted()) {
            throw new RuntimeException(
                    "Cannot update a deleted comment");
        }
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(int commentId) {
        Comment comment = getCommentById(commentId);
        // Soft delete
        comment.setIsDeleted(true);
        comment.setContent("This comment was deleted");
        commentRepository.save(comment);
    }

    @Override
    public long getCommentCount(int cardId) {
        return commentRepository.countByCardId(cardId);
    }

    @Override
    public Attachment addAttachment(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    @Override
    public List<Attachment> getAttachmentsByCard(
            int cardId) {
        return attachmentRepository.findByCardId(cardId);
    }

    @Override
    @Transactional
    public void deleteAttachment(int attachmentId) {
        attachmentRepository
                .deleteByAttachmentId(attachmentId);
    }
}