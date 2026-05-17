package com.flowboard.board.serviceimpl;

import com.flowboard.board.dto.BoardDto;
import com.flowboard.board.dto.BoardMemberDto;
import com.flowboard.board.entity.Board;
import com.flowboard.board.entity.BoardMember;
import com.flowboard.board.exception.BadRequestException;
import com.flowboard.board.exception.ResourceNotFoundException;
import com.flowboard.board.mapper.BoardMapper;
import com.flowboard.board.mapper.BoardMemberMapper;
import com.flowboard.board.repository.BoardMemberRepository;
import com.flowboard.board.repository.BoardRepository;
import com.flowboard.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;

    private Board findBoardById(int boardId) {
        return boardRepository.findByBoardId(boardId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Board not found with id: " + boardId));
    }

    @Override
    public BoardDto createBoard(BoardDto boardDto) {
        Board board = BoardMapper.mapToEntity(boardDto);
        board.setIsClosed(false);
        Board saved = boardRepository.save(board);

        BoardMember creatorMember = new BoardMember();
        creatorMember.setBoardId(saved.getBoardId());
        creatorMember.setUserId(saved.getCreatedById());
        creatorMember.setRole("ADMIN");
        boardMemberRepository.save(creatorMember);

        return BoardMapper.mapToDto(saved);
    }

    @Override
    public BoardDto getBoardById(int boardId) {
        return BoardMapper.mapToDto(findBoardById(boardId));
    }

    @Override
    public List<BoardDto> getBoardsByWorkspace(int workspaceId) {
        return boardRepository.findByWorkspaceId(workspaceId).stream()
                .map(BoardMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardDto> getBoardsByMember(int userId) {
        return boardRepository.findByMemberUserId(userId).stream()
                .map(BoardMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardDto> getBoardsByCreator(int createdById) {
        return boardRepository.findByCreatedById(createdById).stream()
                .map(BoardMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BoardDto updateBoard(int boardId, BoardDto updated) {
        Board existing = findBoardById(boardId);
        if (existing.getIsClosed()) {
            throw new BadRequestException("Cannot update a closed board with id: " + boardId);
        }
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getBackground() != null) existing.setBackground(updated.getBackground());
        if (updated.getVisibility() != null) existing.setVisibility(updated.getVisibility());
        return BoardMapper.mapToDto(boardRepository.save(existing));
    }

    @Override
    public void closeBoard(int boardId) {
        Board board = findBoardById(boardId);
        board.setIsClosed(true);
        boardRepository.save(board);
    }

    @Override
    public void reopenBoard(int boardId) {
        Board board = findBoardById(boardId);
        board.setIsClosed(false);
        boardRepository.save(board);
    }

    @Override
    @Transactional
    public void deleteBoard(int boardId) {
        findBoardById(boardId);
        boardRepository.deleteById(boardId);
    }

    @Override
    public BoardMemberDto addMember(int boardId, int userId, String role) {
        if (boardMemberRepository.existsByBoardIdAndUserId(boardId, userId)) {
            throw new BadRequestException("User " + userId + " is already a member of board " + boardId);
        }
        BoardMember member = new BoardMember();
        member.setBoardId(boardId);
        member.setUserId(userId);
        member.setRole(role != null ? role : "MEMBER");
        return BoardMemberMapper.mapToDto(boardMemberRepository.save(member));
    }

    @Override
    @Transactional
    public void removeMember(int boardId, int userId) {
        if (!boardMemberRepository.existsByBoardIdAndUserId(boardId, userId)) {
            throw new ResourceNotFoundException("Member " + userId + " not found in board " + boardId);
        }
        boardMemberRepository.deleteByBoardIdAndUserId(boardId, userId);
    }

    @Override
    public void updateMemberRole(int boardId, int userId, String role) {
        BoardMember member = boardMemberRepository
                .findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Member " + userId + " not found in board " + boardId));
        member.setRole(role);
        boardMemberRepository.save(member);
    }

    @Override
    public List<BoardMemberDto> getMembers(int boardId) {
        return boardMemberRepository.findByBoardId(boardId).stream()
                .map(BoardMemberMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalCount() {
        return boardRepository.count();
    }

    @Override
    public List<BoardDto> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(BoardMapper::mapToDto)
                .collect(Collectors.toList());
    }
}