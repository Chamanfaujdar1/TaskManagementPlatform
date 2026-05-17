package com.flowboard.workspace.serviceimpl;

import com.flowboard.workspace.dto.WorkspaceDto;
import com.flowboard.workspace.dto.WorkspaceMemberDto;
import com.flowboard.workspace.entity.Workspace;
import com.flowboard.workspace.entity.WorkspaceMember;
import com.flowboard.workspace.exception.BadRequestException;
import com.flowboard.workspace.exception.ResourceNotFoundException;
import com.flowboard.workspace.mapper.WorkspaceMapper;
import com.flowboard.workspace.mapper.WorkspaceMemberMapper;
import com.flowboard.workspace.repository.WorkspaceMemberRepository;
import com.flowboard.workspace.repository.WorkspaceRepository;
import com.flowboard.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    private Workspace findWorkspaceById(int id) {
        return workspaceRepository.findByWorkspaceId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Workspace not found with id: " + id));
    }

    @Override
    public WorkspaceDto createWorkspace(WorkspaceDto workspaceDto) {
        Workspace workspace = WorkspaceMapper.mapToEntity(workspaceDto);
        if (workspace.getName() == null || workspace.getName().trim().isEmpty()) {
            throw new BadRequestException("Workspace name cannot be empty");
        }
        if (workspaceRepository.existsByNameAndOwnerId(workspace.getName(), workspace.getOwnerId())) {
            throw new BadRequestException("Workspace '" + workspace.getName() + "' already exists for this owner");
        }
        Workspace saved = workspaceRepository.save(workspace);

        WorkspaceMember ownerMember = new WorkspaceMember();
        ownerMember.setWorkspaceId(saved.getWorkspaceId());
        ownerMember.setUserId(saved.getOwnerId());
        ownerMember.setRole("ADMIN");
        workspaceMemberRepository.save(ownerMember);

        return WorkspaceMapper.mapToDto(saved);
    }

    @Override
    public WorkspaceDto getById(int workspaceId) {
        return WorkspaceMapper.mapToDto(findWorkspaceById(workspaceId));
    }

    @Override
    public List<WorkspaceDto> getByOwner(int ownerId) {
        return workspaceRepository.findByOwnerId(ownerId).stream()
                .map(WorkspaceMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkspaceDto> getByMember(int userId) {
        return workspaceRepository.findByMemberUserId(userId).stream()
                .map(WorkspaceMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkspaceDto> getPublicWorkspaces() {
        return workspaceRepository.findByVisibility("PUBLIC").stream()
                .map(WorkspaceMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceDto updateWorkspace(int workspaceId, WorkspaceDto updated) {
        Workspace existing = findWorkspaceById(workspaceId);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getVisibility() != null) existing.setVisibility(updated.getVisibility());
        if (updated.getLogoUrl() != null) existing.setLogoUrl(updated.getLogoUrl());
        return WorkspaceMapper.mapToDto(workspaceRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteWorkspace(int workspaceId) {
        findWorkspaceById(workspaceId);
        workspaceRepository.deleteById(workspaceId);
    }

    @Override
    public WorkspaceMemberDto addMember(int workspaceId, int userId, String role) {
        if (workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, userId)) {
            throw new BadRequestException("User " + userId + " is already a member of workspace " + workspaceId);
        }
        WorkspaceMember member = new WorkspaceMember();
        member.setWorkspaceId(workspaceId);
        member.setUserId(userId);
        member.setRole(role != null ? role : "MEMBER");
        return WorkspaceMemberMapper.mapToDto(workspaceMemberRepository.save(member));
    }

    @Override
    @Transactional
    public void removeMember(int workspaceId, int userId) {
        if (!workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, userId)) {
            throw new ResourceNotFoundException("Member " + userId + " not found in workspace " + workspaceId);
        }
        workspaceMemberRepository.deleteByWorkspaceIdAndUserId(workspaceId, userId);
    }

    @Override
    public void updateMemberRole(int workspaceId, int userId, String role) {
        WorkspaceMember member = workspaceMemberRepository
                .findByWorkspaceIdAndUserId(workspaceId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Member " + userId + " not found in workspace " + workspaceId));
        member.setRole(role);
        workspaceMemberRepository.save(member);
    }

    @Override
    public List<WorkspaceMemberDto> getMembers(int workspaceId) {
        return workspaceMemberRepository.findByWorkspaceId(workspaceId).stream()
                .map(WorkspaceMemberMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalCount() {
        return workspaceRepository.count();
    }

    @Override
    public List<WorkspaceDto> getAllWorkspaces() {
        return workspaceRepository.findAll().stream()
                .map(WorkspaceMapper::mapToDto)
                .collect(Collectors.toList());
    }
}