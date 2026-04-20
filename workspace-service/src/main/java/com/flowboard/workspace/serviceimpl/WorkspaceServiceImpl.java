package com.flowboard.workspace.serviceimpl;

import com.flowboard.workspace.entity.Workspace;
import com.flowboard.workspace.entity.WorkspaceMember;
import com.flowboard.workspace.repository.WorkspaceMemberRepository;
import com.flowboard.workspace.repository.WorkspaceRepository;
import com.flowboard.workspace.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private WorkspaceMemberRepository workspaceMemberRepository;

    @Override
    public Workspace createWorkspace(Workspace workspace) {
        if (workspaceRepository.existsByNameAndOwnerId(
                workspace.getName(), workspace.getOwnerId())) {
            throw new RuntimeException(
                    "Workspace with this name already exists");
        }
        Workspace saved = workspaceRepository.save(workspace);

        // Auto add owner as ADMIN member
        WorkspaceMember ownerMember = new WorkspaceMember();
        ownerMember.setWorkspaceId(saved.getWorkspaceId());
        ownerMember.setUserId(saved.getOwnerId());
        ownerMember.setRole("ADMIN");
        workspaceMemberRepository.save(ownerMember);

        return saved;
    }

    @Override
    public Workspace getById(int workspaceId) {
        return workspaceRepository.findByWorkspaceId(workspaceId)
                .orElseThrow(() ->
                        new RuntimeException("Workspace not found"));
    }

    @Override
    public List<Workspace> getByOwner(int ownerId) {
        return workspaceRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Workspace> getByMember(int userId) {
        return workspaceRepository.findByMemberUserId(userId);
    }

    @Override
    public List<Workspace> getPublicWorkspaces() {
        return workspaceRepository.findByVisibility("PUBLIC");
    }

    @Override
    public Workspace updateWorkspace(int workspaceId, Workspace updated) {
        Workspace existing = getById(workspaceId);
        if (updated.getName() != null) {
            existing.setName(updated.getName());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription());
        }
        if (updated.getVisibility() != null) {
            existing.setVisibility(updated.getVisibility());
        }
        if (updated.getLogoUrl() != null) {
            existing.setLogoUrl(updated.getLogoUrl());
        }
        return workspaceRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteWorkspace(int workspaceId) {
        getById(workspaceId);
        workspaceRepository.deleteById(workspaceId);
    }

    @Override
    public WorkspaceMember addMember(
            int workspaceId, int userId, String role) {
        if (workspaceMemberRepository
                .existsByWorkspaceIdAndUserId(workspaceId, userId)) {
            throw new RuntimeException(
                    "User is already a member of this workspace");
        }
        WorkspaceMember member = new WorkspaceMember();
        member.setWorkspaceId(workspaceId);
        member.setUserId(userId);
        member.setRole(role != null ? role : "MEMBER");
        return workspaceMemberRepository.save(member);
    }

    @Override
    @Transactional
    public void removeMember(int workspaceId, int userId) {
        if (!workspaceMemberRepository
                .existsByWorkspaceIdAndUserId(workspaceId, userId)) {
            throw new RuntimeException("Member not found");
        }
        workspaceMemberRepository
                .deleteByWorkspaceIdAndUserId(workspaceId, userId);
    }

    @Override
    public void updateMemberRole(
            int workspaceId, int userId, String role) {
        WorkspaceMember member = workspaceMemberRepository
                .findByWorkspaceIdAndUserId(workspaceId, userId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));
        member.setRole(role);
        workspaceMemberRepository.save(member);
    }

    @Override
    public List<WorkspaceMember> getMembers(int workspaceId) {
        return workspaceMemberRepository.findByWorkspaceId(workspaceId);
    }
}