package com.flowboard.comment.service;

import com.flowboard.comment.entity.Attachment;
import com.flowboard.comment.entity.Comment;
import java.util.List;

public interface CommentService {

    Comment addComment(Comment comment);

    List<Comment> getByCard(int cardId);

    Comment getCommentById(int commentId);

    List<Comment> getReplies(int parentCommentId);

    Comment updateComment(int commentId, String content);

    void deleteComment(int commentId);

    long getCommentCount(int cardId);

    Attachment addAttachment(Attachment attachment);

    List<Attachment> getAttachmentsByCard(int cardId);

    void deleteAttachment(int attachmentId);
}