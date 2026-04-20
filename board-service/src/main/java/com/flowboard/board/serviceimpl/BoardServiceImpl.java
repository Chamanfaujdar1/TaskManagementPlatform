package com.flowboard.board.serviceimpl;

import com.flowboard.board.entity.Board;
import com.flowboard.board.entity.BoardMember;
import com.flowboard.board.repository.BoardMemberRepository;
import com.flowboard.board.repository.BoardRepository;
import com.flowboard.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardMemberRepository boardMemberRepository;

    @Override
    public Board createBoard(Board board) {
        board.setIsClosed(false);
        Board saved = boardRepository.save(board);

        // Auto add creator as ADMIN member
        BoardMember creatorMember = new BoardMember();
        creatorMember.setBoardId(saved.getBoardId());
        creatorMember.setUserId(saved.getCreatedById());
        creatorMember.setRole("ADMIN");
        boardMemberRepository.save(creatorMember);

        return saved;
    }

    @Override
    public Board getBoardById(int boardId) {
        return boardRepository.findByBoardId(boardId)
                .orElseThrow(() ->
                        new RuntimeException("Board not found"));
    }

    @Override
    public List<Board> getBoardsByWorkspace(int workspaceId) {
        return boardRepository.findByWorkspaceId(workspaceId);
    }

    @Override
    public List<Board> getBoardsByMember(int userId) {
        return boardRepository.findByMemberUserId(userId);
    }

    @Override
    public List<Board> getBoardsByCreator(int createdById) {
        return boardRepository.findByCreatedById(createdById);
    }

    @Override
    public Board updateBoard(int boardId, Board updated) {
        Board existing = getBoardById(boardId);
        if (existing.getIsClosed()) {
            throw new RuntimeException(
                    "Cannot update a closed board");
        }
        if (updated.getName() != null) {
            existing.setName(updated.getName());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription());
        }
        if (updated.getBackground() != null) {
            existing.setBackground(updated.getBackground());
        }
        if (updated.getVisibility() != null) {
            existing.setVisibility(updated.getVisibility());
        }
        return boardRepository.save(existing);
    }

    @Override
    public void closeBoard(int boardId) {
        Board board = getBoardById(boardId);
        board.setIsClosed(true);
        boardRepository.save(board);
    }

    @Override
    @Transactional
    public void deleteBoard(int boardId) {
        getBoardById(boardId);
        boardRepository.deleteById(boardId);
    }

    @Override
    public BoardMember addMember(
            int boardId, int userId, String role) {
        if (boardMemberRepository
                .existsByBoardIdAndUserId(boardId, userId)) {
            throw new RuntimeException(
                    "User is already a member of this board");
        }
        BoardMember member = new BoardMember();
        member.setBoardId(boardId);
        member.setUserId(userId);
        member.setRole(role != null ? role : "MEMBER");
        return boardMemberRepository.save(member);
    }

    @Override
    @Transactional
    public void removeMember(int boardId, int userId) {
        if (!boardMemberRepository
                .existsByBoardIdAndUserId(boardId, userId)) {
            throw new RuntimeException("Member not found");
        }
        boardMemberRepository
                .deleteByBoardIdAndUserId(boardId, userId);
    }

    @Override
    public void updateMemberRole(
            int boardId, int userId, String role) {
        BoardMember member = boardMemberRepository
                .findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));
        member.setRole(role);
        boardMemberRepository.save(member);
    }

    @Override
    public List<BoardMember> getMembers(int boardId) {
        return boardMemberRepository.findByBoardId(boardId);
    }
}