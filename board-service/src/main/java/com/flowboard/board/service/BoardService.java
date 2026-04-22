package com.flowboard.board.service;

import com.flowboard.board.entity.Board;
import com.flowboard.board.entity.BoardMember;

import java.util.List;

public interface BoardService {

    Board createBoard(Board board);

    Board getBoardById(int boardId);

    List<Board> getBoardsByWorkspace(int workspaceId);

    List<Board> getBoardsByMember(int userId);

    List<Board> getBoardsByCreator(int createdById);

    Board updateBoard(int boardId, Board board);

    void closeBoard(int boardId);

    void deleteBoard(int boardId);

    BoardMember addMember(int boardId, int userId, String role);

    void removeMember(int boardId, int userId);

    void updateMemberRole(int boardId, int userId, String role);

    List<BoardMember> getMembers(int boardId);
}