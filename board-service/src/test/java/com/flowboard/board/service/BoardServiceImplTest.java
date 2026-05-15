package com.flowboard.board.service;

import com.flowboard.board.entity.Board;
import com.flowboard.board.entity.BoardMember;
import com.flowboard.board.repository.BoardMemberRepository;
import com.flowboard.board.repository.BoardRepository;
import com.flowboard.board.serviceimpl.BoardServiceImpl;
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
public class BoardServiceImplTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardMemberRepository boardMemberRepository;

    @InjectMocks
    private BoardServiceImpl boardService;

    private Board testBoard;

    @BeforeEach
    void setUp() {
        testBoard = new Board();
        testBoard.setBoardId(1);
        testBoard.setName("Test Board");
        testBoard.setWorkspaceId(10);
        testBoard.setCreatedById(100);
    }

    @Test
    void createBoard_Success() {
        // Arrange
        when(boardRepository.save(any(Board.class))).thenReturn(testBoard);

        // Act
        Board saved = boardService.createBoard(testBoard);

        // Assert
        assertNotNull(saved);
        assertFalse(saved.getIsClosed());
        verify(boardRepository, times(1)).save(testBoard);
        verify(boardMemberRepository, times(1)).save(any(BoardMember.class));
    }
}
