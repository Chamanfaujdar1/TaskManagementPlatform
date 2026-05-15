package com.flowboard.workspace.service;

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

    @BeforeEach
    void setUp() {
        testWorkspace = new Workspace();
        testWorkspace.setWorkspaceId(1);
        testWorkspace.setName("Test Workspace");
        testWorkspace.setOwnerId(100);
        testWorkspace.setVisibility("PRIVATE");
    }

    @Test
    void createWorkspace_Success() {
        // Arrange
        when(workspaceRepository.existsByNameAndOwnerId(anyString(), anyInt())).thenReturn(false);
        when(workspaceRepository.save(any(Workspace.class))).thenReturn(testWorkspace);

        // Act
        Workspace saved = workspaceService.createWorkspace(testWorkspace);

        // Assert
        assertNotNull(saved);
        assertEquals("Test Workspace", saved.getName());
        verify(workspaceRepository, times(1)).save(testWorkspace);
        verify(workspaceMemberRepository, times(1)).save(any(WorkspaceMember.class));
    }

    @Test
    void createWorkspace_EmptyName_ThrowsException() {
        // Arrange
        testWorkspace.setName("");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> workspaceService.createWorkspace(testWorkspace));
        verify(workspaceRepository, never()).save(any(Workspace.class));
    }

    @Test
    void createWorkspace_AlreadyExists_ThrowsException() {
        // Arrange
        when(workspaceRepository.existsByNameAndOwnerId(anyString(), anyInt())).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> workspaceService.createWorkspace(testWorkspace));
        verify(workspaceRepository, never()).save(any(Workspace.class));
    }
}
