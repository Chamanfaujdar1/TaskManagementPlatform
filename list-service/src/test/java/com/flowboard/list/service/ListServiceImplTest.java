package com.flowboard.list.service;

import com.flowboard.list.dto.TaskListDto;
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
    private TaskListDto testListDto;

    @BeforeEach
    void setUp() {
        testList = new TaskList();
        testList.setListId(1);
        testList.setName("To Do");
        testList.setBoardId(10);
        testList.setPosition(6);
        testList.setIsArchived(false);

        testListDto = new TaskListDto();
        testListDto.setListId(1);
        testListDto.setName("To Do");
        testListDto.setBoardId(10);
    }

    @Test
    void createList_Success() {
        // Arrange
        when(listRepository.findMaxPositionByBoardId(anyInt())).thenReturn(5);
        when(listRepository.save(any(TaskList.class))).thenReturn(testList);

        // Act
        TaskListDto saved = listService.createList(testListDto);

        // Assert
        assertNotNull(saved);
        assertEquals(6, saved.getPosition());
        assertFalse(saved.getIsArchived());
        verify(listRepository, times(1)).save(any(TaskList.class));
    }

    @Test
    void createList_EmptyName_ThrowsException() {
        // Arrange
        testListDto.setName("");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> listService.createList(testListDto));
    }
}
