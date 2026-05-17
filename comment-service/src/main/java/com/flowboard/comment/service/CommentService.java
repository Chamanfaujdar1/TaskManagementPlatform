package com.flowboard.comment.service;

import com.flowboard.comment.dto.AttachmentDto;
import com.flowboard.comment.dto.CommentDto;
import java.util.List;

public interface CommentService {

    CommentDto addComment(CommentDto commentDto);

    List<CommentDto> getByCard(int cardId);

    CommentDto getCommentById(int commentId);

    List<CommentDto> getReplies(int parentCommentId);

    CommentDto updateComment(int commentId, String content);

    void deleteComment(int commentId);

    long getCommentCount(int cardId);

    AttachmentDto addAttachment(AttachmentDto attachmentDto);

    List<AttachmentDto> getAttachmentsByCard(int cardId);

    void deleteAttachment(int attachmentId);
}