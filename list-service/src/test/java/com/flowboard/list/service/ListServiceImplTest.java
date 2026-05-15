package com.flowboard.list.service;

import com.flowboard.list.entity.TaskList;
import com.flowboard.list.exception.BadRequestException;
import com.flowboard.list.repository.ListRepository;
import com.flowboard.list.serviceimpl.ListServiceImpl;
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
public class ListServiceImplTest {

    @Mock
    private ListRepository listRepository;

    @InjectMocks
    private ListServiceImpl listService;

    private TaskList testList;

    @BeforeEach
    void setUp() {
        testList = new TaskList();
        testList.setListId(1);
        testList.setName("To Do");
        testList.setBoardId(10);
    }

    @Test
    void createList_Success() {
        // Arrange
        when(listRepository.findMaxPositionByBoardId(anyInt())).thenReturn(5);
        when(listRepository.save(any(TaskList.class))).thenReturn(testList);

        // Act
        TaskList saved = listService.createList(testList);

        // Assert
        assertNotNull(saved);
        assertEquals(6, testList.getPosition());
        assertFalse(testList.getIsArchived());
        verify(listRepository, times(1)).save(testList);
    }

    @Test
    void createList_EmptyName_ThrowsException() {
        // Arrange
        testList.setName("");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> listService.createList(testList));
    }
}
