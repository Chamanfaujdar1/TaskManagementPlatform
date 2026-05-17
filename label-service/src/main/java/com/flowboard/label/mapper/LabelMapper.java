package com.flowboard.label.mapper;

import com.flowboard.label.dto.CardLabelDto;
import com.flowboard.label.dto.ChecklistDto;
import com.flowboard.label.dto.ChecklistItemDto;
import com.flowboard.label.dto.LabelDto;
import com.flowboard.label.entity.CardLabel;
import com.flowboard.label.entity.Checklist;
import com.flowboard.label.entity.ChecklistItem;
import com.flowboard.label.entity.Label;

public class LabelMapper {

    public static LabelDto mapToDto(Label entity) {
        if (entity == null) return null;
        LabelDto dto = new LabelDto();
        dto.setLabelId(entity.getLabelId());
        dto.setBoardId(entity.getBoardId());
        dto.setName(entity.getName());
        dto.setColor(entity.getColor());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static Label mapToEntity(LabelDto dto) {
        if (dto == null) return null;
        Label entity = new Label();
        entity.setLabelId(dto.getLabelId());
        entity.setBoardId(dto.getBoardId());
        entity.setName(dto.getName());
        entity.setColor(dto.getColor());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public static ChecklistDto mapToDto(Checklist entity) {
        if (entity == null) return null;
        ChecklistDto dto = new ChecklistDto();
        dto.setChecklistId(entity.getChecklistId());
        dto.setCardId(entity.getCardId());
        dto.setTitle(entity.getTitle());
        dto.setPosition(entity.getPosition());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static Checklist mapToEntity(ChecklistDto dto) {
        if (dto == null) return null;
        Checklist entity = new Checklist();
        entity.setChecklistId(dto.getChecklistId());
        entity.setCardId(dto.getCardId());
        entity.setTitle(dto.getTitle());
        entity.setPosition(dto.getPosition());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public static ChecklistItemDto mapToDto(ChecklistItem entity) {
        if (entity == null) return null;
        ChecklistItemDto dto = new ChecklistItemDto();
        dto.setItemId(entity.getItemId());
        dto.setChecklistId(entity.getChecklistId());
        dto.setText(entity.getText());
        dto.setIsCompleted(entity.getIsCompleted());
        dto.setAssigneeId(entity.getAssigneeId());
        dto.setDueDate(entity.getDueDate());
        return dto;
    }

    public static ChecklistItem mapToEntity(ChecklistItemDto dto) {
        if (dto == null) return null;
        ChecklistItem entity = new ChecklistItem();
        entity.setItemId(dto.getItemId());
        entity.setChecklistId(dto.getChecklistId());
        entity.setText(dto.getText());
        entity.setIsCompleted(dto.getIsCompleted());
        entity.setAssigneeId(dto.getAssigneeId());
        entity.setDueDate(dto.getDueDate());
        return entity;
    }

    public static CardLabelDto mapToDto(CardLabel entity) {
        if (entity == null) return null;
        CardLabelDto dto = new CardLabelDto();
        dto.setId(entity.getId());
        dto.setCardId(entity.getCardId());
        dto.setLabelId(entity.getLabelId());
        return dto;
    }

    public static CardLabel mapToEntity(CardLabelDto dto) {
        if (dto == null) return null;
        CardLabel entity = new CardLabel();
        entity.setId(dto.getId());
        entity.setCardId(dto.getCardId());
        entity.setLabelId(dto.getLabelId());
        return entity;
    }
}
