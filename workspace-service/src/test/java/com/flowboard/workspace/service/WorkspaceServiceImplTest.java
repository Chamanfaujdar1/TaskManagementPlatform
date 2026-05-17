package com.flowboard.workspace.service;

import com.flowboard.workspace.dto.WorkspaceDto;
import com.flowboard.workspace.entity.Workspace;
import com.flowboard.workspace.entity.WorkspaceMember;
import com.flowboard.workspace.exception.BadRequestException;
import com.flowboard.workspace.repository.WorkspaceMemberRepository;
import com.flowboard.workspace.repository.WorkspaceRepository;
import com.flowboard.workspace.serviceimpl.WorkspaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkspaceServiceImplTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private WorkspaceMemberRepository workspaceMemberRepository;

    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    private Workspace testWorkspace;
    private WorkspaceDto testWorkspaceDto;

    @BeforeEach
    void setUp() {
        testWorkspace = new Workspace();
        testWorkspace.setWorkspaceId(1);
        testWorkspace.setName("Test Workspace");
        testWorkspace.setOwnerId(100);
        testWorkspace.setVisibility("PRIVATE");

        testWorkspaceDto = new WorkspaceDto();
        testWorkspaceDto.setWorkspaceId(1);
        testWorkspaceDto.setName("Test Workspace");
        testWorkspaceDto.setOwnerId(100);
        testWorkspaceDto.setVisibility("PRIVATE");
    }

    @Test
    void createWorkspace_Success() {
        // Arrange
        when(workspaceRepository.existsByNameAndOwnerId(anyString(), anyInt())).thenReturn(false);
        when(workspaceRepository.save(any(Workspace.class))).thenReturn(testWorkspace);

        // Act
        WorkspaceDto saved = workspaceService.createWorkspace(testWorkspaceDto);

        // Assert
        assertNotNull(saved);
        assertEquals("Test Workspace", saved.getName());
        verify(workspaceRepository, times(1)).save(any(Workspace.class));
        verify(workspaceMemberRepository, times(1)).save(any(WorkspaceMember.class));
    }

    @Test
    void createWorkspace_EmptyName_ThrowsException() {
        // Arrange
        testWorkspaceDto.setName("");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> workspaceService.createWorkspace(testWorkspaceDto));
        verify(workspaceRepository, never()).save(any(Workspace.class));
    }

    @Test
    void createWorkspace_AlreadyExists_ThrowsException() {
        // Arrange
        when(workspaceRepository.existsByNameAndOwnerId(anyString(), anyInt())).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> workspaceService.createWorkspace(testWorkspaceDto));
        verify(workspaceRepository, never()).save(any(Workspace.class));
    }
}
