package com.flowboard.comment.mapper;

import com.flowboard.comment.dto.AttachmentDto;
import com.flowboard.comment.dto.CommentDto;
import com.flowboard.comment.entity.Attachment;
import com.flowboard.comment.entity.Comment;

public class CommentMapper {

    public static CommentDto mapToDto(Comment entity) {
        if (entity == null) return null;
        CommentDto dto = new CommentDto();
        dto.setCommentId(entity.getCommentId());
        dto.setCardId(entity.getCardId());
        dto.setAuthorId(entity.getAuthorId());
        dto.setContent(entity.getContent());
        dto.setParentCommentId(entity.getParentCommentId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setIsDeleted(entity.getIsDeleted());
        return dto;
    }

    public static Comment mapToEntity(CommentDto dto) {
        if (dto == null) return null;
        Comment entity = new Comment();
        entity.setCommentId(dto.getCommentId());
        entity.setCardId(dto.getCardId());
        entity.setAuthorId(dto.getAuthorId());
        entity.setContent(dto.getContent());
        entity.setParentCommentId(dto.getParentCommentId());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setIsDeleted(dto.getIsDeleted());
        return entity;
    }

    public static AttachmentDto mapToDto(Attachment entity) {
        if (entity == null) return null;
        AttachmentDto dto = new AttachmentDto();
        dto.setAttachmentId(entity.getAttachmentId());
        dto.setCardId(entity.getCardId());
        dto.setUploaderId(entity.getUploaderId());
        dto.setFileName(entity.getFileName());
        dto.setFileUrl(entity.getFileUrl());
        dto.setFileType(entity.getFileType());
        dto.setSizeKb(entity.getSizeKb());
        dto.setUploadedAt(entity.getUploadedAt());
        return dto;
    }

    public static Attachment mapToEntity(AttachmentDto dto) {
        if (dto == null) return null;
        Attachment entity = new Attachment();
        entity.setAttachmentId(dto.getAttachmentId());
        entity.setCardId(dto.getCardId());
        entity.setUploaderId(dto.getUploaderId());
        entity.setFileName(dto.getFileName());
        entity.setFileUrl(dto.getFileUrl());
        entity.setFileType(dto.getFileType());
        entity.setSizeKb(dto.getSizeKb());
        entity.setUploadedAt(dto.getUploadedAt());
        return entity;
    }
}
