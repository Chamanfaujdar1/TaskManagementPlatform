package com.flowboard.workspace.mapper;

import com.flowboard.workspace.dto.WorkspaceMemberDto;
import com.flowboard.workspace.entity.WorkspaceMember;

public class WorkspaceMemberMapper {

    public static WorkspaceMemberDto mapToDto(WorkspaceMember entity) {
        if (entity == null) {
            return null;
        }
        WorkspaceMemberDto dto = new WorkspaceMemberDto();
        dto.setMemberId(entity.getMemberId());
        dto.setWorkspaceId(entity.getWorkspaceId());
        dto.setUserId(entity.getUserId());
        dto.setRole(entity.getRole());
        dto.setJoinedAt(entity.getJoinedAt());
        return dto;
    }

    public static WorkspaceMember mapToEntity(WorkspaceMemberDto dto) {
        if (dto == null) {
            return null;
        }
        WorkspaceMember entity = new WorkspaceMember();
        entity.setMemberId(dto.getMemberId());
        entity.setWorkspaceId(dto.getWorkspaceId());
        entity.setUserId(dto.getUserId());
        entity.setRole(dto.getRole());
        entity.setJoinedAt(dto.getJoinedAt());
        return entity;
    }
}
