package com.flowboard.card.mapper;

import com.flowboard.card.dto.CardDto;
import com.flowboard.card.entity.Card;

public class CardMapper {

    public static CardDto mapToDto(Card entity) {
        if (entity == null) {
            return null;
        }
        CardDto dto = new CardDto();
        dto.setCardId(entity.getCardId());
        dto.setListId(entity.getListId());
        dto.setBoardId(entity.getBoardId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPosition(entity.getPosition());
        dto.setPriority(entity.getPriority());
        dto.setStatus(entity.getStatus());
        dto.setDueDate(entity.getDueDate());
        dto.setStartDate(entity.getStartDate());
        dto.setAssigneeId(entity.getAssigneeId());
        dto.setCreatedById(entity.getCreatedById());
        dto.setIsArchived(entity.getIsArchived());
        dto.setOneDayReminderSent(entity.getOneDayReminderSent());
        dto.setOneHourReminderSent(entity.getOneHourReminderSent());
        dto.setCoverColor(entity.getCoverColor());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static Card mapToEntity(CardDto dto) {
        if (dto == null) {
            return null;
        }
        Card entity = new Card();
        entity.setCardId(dto.getCardId());
        entity.setListId(dto.getListId());
        entity.setBoardId(dto.getBoardId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setPosition(dto.getPosition());
        entity.setPriority(dto.getPriority());
        entity.setStatus(dto.getStatus());
        entity.setDueDate(dto.getDueDate());
        entity.setStartDate(dto.getStartDate());
        entity.setAssigneeId(dto.getAssigneeId());
        entity.setCreatedById(dto.getCreatedById());
        entity.setIsArchived(dto.getIsArchived());
        entity.setOneDayReminderSent(dto.getOneDayReminderSent());
        entity.setOneHourReminderSent(dto.getOneHourReminderSent());
        entity.setCoverColor(dto.getCoverColor());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
