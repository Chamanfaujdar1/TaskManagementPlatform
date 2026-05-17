package com.flowboard.list.mapper;

import com.flowboard.list.dto.TaskListDto;
import com.flowboard.list.entity.TaskList;

public class TaskListMapper {

    public static TaskListDto mapToDto(TaskList entity) {
        if (entity == null) {
            return null;
        }
        TaskListDto dto = new TaskListDto();
        dto.setListId(entity.getListId());
        dto.setBoardId(entity.getBoardId());
        dto.setName(entity.getName());
        dto.setPosition(entity.getPosition());
        dto.setColor(entity.getColor());
        dto.setIsArchived(entity.getIsArchived());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static TaskList mapToEntity(TaskListDto dto) {
        if (dto == null) {
            return null;
        }
        TaskList entity = new TaskList();
        entity.setListId(dto.getListId());
        entity.setBoardId(dto.getBoardId());
        entity.setName(dto.getName());
        entity.setPosition(dto.getPosition());
        entity.setColor(dto.getColor());
        entity.setIsArchived(dto.getIsArchived());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
