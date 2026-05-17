package com.flowboard.comment.serviceimpl;

import com.flowboard.comment.dto.AttachmentDto;
import com.flowboard.comment.dto.CommentDto;
import com.flowboard.comment.entity.Attachment;
import com.flowboard.comment.entity.Comment;
import com.flowboard.comment.exception.BadRequestException;
import com.flowboard.comment.exception.ResourceNotFoundException;
import com.flowboard.comment.mapper.CommentMapper;
import com.flowboard.comment.repository.AttachmentRepository;
import com.flowboard.comment.repository.CommentRepository;
import com.flowboard.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AttachmentRepository attachmentRepository;

    private Comment findCommentById(int commentId) {
        return commentRepository.findByCommentId(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment not found with id: " + commentId));
    }

    @Override
    public CommentDto addComment(CommentDto commentDto) {
        Comment comment = CommentMapper.mapToEntity(commentDto);
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new BadRequestException("Comment content cannot be empty");
        }
        comment.setIsDeleted(false);
        return CommentMapper.mapToDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getByCard(int cardId) {
        return commentRepository.findByCardId(cardId).stream()
                .map(CommentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(int commentId) {
        return CommentMapper.mapToDto(findCommentById(commentId));
    }

    @Override
    public List<CommentDto> getReplies(int parentCommentId) {
        return commentRepository.findByParentCommentId(parentCommentId).stream()
                .map(CommentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto updateComment(int commentId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BadRequestException("Comment content cannot be empty");
        }
        Comment comment = findCommentById(commentId);
        if (comment.getIsDeleted()) {
            throw new BadRequestException("Cannot update a deleted comment");
        }
        comment.setContent(content);
        return CommentMapper.mapToDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(int commentId) {
        Comment comment = findCommentById(commentId);
        if (comment.getIsDeleted()) {
            throw new BadRequestException("Comment is already deleted");
        }
        comment.setIsDeleted(true);
        comment.setContent("This comment was deleted");
        commentRepository.save(comment);
    }

    @Override
    public long getCommentCount(int cardId) {
        return commentRepository.countByCardId(cardId);
    }

    @Override
    public AttachmentDto addAttachment(AttachmentDto attachmentDto) {
        Attachment attachment = CommentMapper.mapToEntity(attachmentDto);
        if (attachment.getFileName() == null || attachment.getFileName().trim().isEmpty()) {
            throw new BadRequestException("Attachment file name cannot be empty");
        }
        return CommentMapper.mapToDto(attachmentRepository.save(attachment));
    }

    @Override
    public List<AttachmentDto> getAttachmentsByCard(int cardId) {
        return attachmentRepository.findByCardId(cardId).stream()
                .map(CommentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAttachment(int attachmentId) {
        attachmentRepository.deleteByAttachmentId(attachmentId);
    }
}