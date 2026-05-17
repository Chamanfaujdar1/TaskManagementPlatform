package com.flowboard.workspace.mapper;

import com.flowboard.workspace.dto.WorkspaceDto;
import com.flowboard.workspace.entity.Workspace;

public class WorkspaceMapper {

    public static WorkspaceDto mapToDto(Workspace entity) {
        if (entity == null) {
            return null;
        }
        WorkspaceDto dto = new WorkspaceDto();
        dto.setWorkspaceId(entity.getWorkspaceId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setOwnerId(entity.getOwnerId());
        dto.setVisibility(entity.getVisibility());
        dto.setLogoUrl(entity.getLogoUrl());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static Workspace mapToEntity(WorkspaceDto dto) {
        if (dto == null) {
            return null;
        }
        Workspace entity = new Workspace();
        entity.setWorkspaceId(dto.getWorkspaceId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOwnerId(dto.getOwnerId());
        entity.setVisibility(dto.getVisibility());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
