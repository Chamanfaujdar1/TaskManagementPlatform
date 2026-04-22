package com.flowboard.workspace.service;

import com.flowboard.workspace.entity.Workspace;
import com.flowboard.workspace.entity.WorkspaceMember;

import java.util.List;

public interface WorkspaceService {

    Workspace createWorkspace(Workspace workspace);

    Workspace getById(int workspaceId);

    List<Workspace> getByOwner(int ownerId);

    List<Workspace> getByMember(int userId);

    List<Workspace> getPublicWorkspaces();

    Workspace updateWorkspace(int workspaceId, Workspace workspace);

    void deleteWorkspace(int workspaceId);

    WorkspaceMember addMember(int workspaceId, int userId, String role);

    void removeMember(int workspaceId, int userId);

    void updateMemberRole(int workspaceId, int userId, String role);

    List<WorkspaceMember> getMembers(int workspaceId);
}