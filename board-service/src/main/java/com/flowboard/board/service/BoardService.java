package com.flowboard.board.service;

import com.flowboard.board.dto.BoardDto;
import com.flowboard.board.dto.BoardMemberDto;

import java.util.List;

public interface BoardService {

    BoardDto createBoard(BoardDto boardDto);

    BoardDto getBoardById(int boardId);

    List<BoardDto> getBoardsByWorkspace(int workspaceId);

    List<BoardDto> getBoardsByMember(int userId);

    List<BoardDto> getBoardsByCreator(int createdById);

    BoardDto updateBoard(int boardId, BoardDto boardDto);

    void closeBoard(int boardId);
    void reopenBoard(int boardId);

    void deleteBoard(int boardId);

    BoardMemberDto addMember(int boardId, int userId, String role);

    void removeMember(int boardId, int userId);

    void updateMemberRole(int boardId, int userId, String role);

    List<BoardMemberDto> getMembers(int boardId);

    long getTotalCount();

    List<BoardDto> getAllBoards();
}