package com.flowboard.workspace.service;

import com.flowboard.workspace.dto.WorkspaceDto;
import com.flowboard.workspace.dto.WorkspaceMemberDto;

import java.util.List;

public interface WorkspaceService {

    WorkspaceDto createWorkspace(WorkspaceDto workspaceDto);

    WorkspaceDto getById(int workspaceId);

    List<WorkspaceDto> getByOwner(int ownerId);

    List<WorkspaceDto> getByMember(int userId);

    List<WorkspaceDto> getPublicWorkspaces();

    WorkspaceDto updateWorkspace(int workspaceId, WorkspaceDto workspaceDto);

    void deleteWorkspace(int workspaceId);

    WorkspaceMemberDto addMember(int workspaceId, int userId, String role);

    void removeMember(int workspaceId, int userId);

    void updateMemberRole(int workspaceId, int userId, String role);

    List<WorkspaceMemberDto> getMembers(int workspaceId);

    long getTotalCount();

    List<WorkspaceDto> getAllWorkspaces();
}