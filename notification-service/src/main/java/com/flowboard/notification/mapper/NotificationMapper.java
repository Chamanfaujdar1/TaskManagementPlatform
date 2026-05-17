package com.flowboard.notification.mapper;

import com.flowboard.notification.dto.NotificationDto;
import com.flowboard.notification.entity.Notification;

public class NotificationMapper {

    public static NotificationDto mapToDto(Notification entity) {
        if (entity == null) return null;
        NotificationDto dto = new NotificationDto();
        dto.setNotificationId(entity.getNotificationId());
        dto.setRecipientId(entity.getRecipientId());
        dto.setActorId(entity.getActorId());
        dto.setType(entity.getType());
        dto.setMessage(entity.getMessage());
        dto.setTitle(entity.getTitle());
        dto.setRelatedId(entity.getRelatedId());
        dto.setRelatedType(entity.getRelatedType());
        dto.setIsRead(entity.getIsRead());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static Notification mapToEntity(NotificationDto dto) {
        if (dto == null) return null;
        Notification entity = new Notification();
        entity.setNotificationId(dto.getNotificationId());
        entity.setRecipientId(dto.getRecipientId());
        entity.setActorId(dto.getActorId());
        entity.setType(dto.getType());
        entity.setMessage(dto.getMessage());
        entity.setTitle(dto.getTitle());
        entity.setRelatedId(dto.getRelatedId());
        entity.setRelatedType(dto.getRelatedType());
        entity.setIsRead(dto.getIsRead());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}
