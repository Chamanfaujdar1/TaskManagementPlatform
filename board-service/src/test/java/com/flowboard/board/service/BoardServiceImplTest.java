package com.flowboard.board.service;

import com.flowboard.board.dto.BoardDto;
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
    private BoardDto testBoardDto;

    @BeforeEach
    void setUp() {
        testBoard = new Board();
        testBoard.setBoardId(1);
        testBoard.setName("Test Board");
        testBoard.setWorkspaceId(10);
        testBoard.setCreatedById(100);
        testBoard.setIsClosed(false);

        testBoardDto = new BoardDto();
        testBoardDto.setBoardId(1);
        testBoardDto.setName("Test Board");
        testBoardDto.setWorkspaceId(10);
        testBoardDto.setCreatedById(100);
        testBoardDto.setIsClosed(false);
    }

    @Test
    void createBoard_Success() {
        // Arrange
        when(boardRepository.save(any(Board.class))).thenReturn(testBoard);

        // Act
        BoardDto saved = boardService.createBoard(testBoardDto);

        // Assert
        assertNotNull(saved);
        assertFalse(saved.getIsClosed());
        verify(boardRepository, times(1)).save(any(Board.class));
        verify(boardMemberRepository, times(1)).save(any(BoardMember.class));
    }
}
