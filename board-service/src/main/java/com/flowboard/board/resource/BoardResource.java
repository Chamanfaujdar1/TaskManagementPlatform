package com.flowboard.board.resource;

import com.flowboard.board.dto.BoardDto;
import com.flowboard.board.dto.BoardMemberDto;
import com.flowboard.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardResource {

    private final BoardService boardService;

    // CREATE BOARD
    @PostMapping
    public ResponseEntity<BoardDto> create(
            @RequestBody BoardDto boardDto) {
        return ResponseEntity.ok(
                boardService.createBoard(boardDto));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getById(
            @PathVariable int id) {
        return ResponseEntity.ok(
                boardService.getBoardById(id));
    }

    // GET BY WORKSPACE
    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<BoardDto>> getByWorkspace(
            @PathVariable int workspaceId) {
        return ResponseEntity.ok(
                boardService.getBoardsByWorkspace(workspaceId));
    }

    // GET BY MEMBER
    @GetMapping("/member/{userId}")
    public ResponseEntity<List<BoardDto>> getByMember(
            @PathVariable int userId) {
        return ResponseEntity.ok(
                boardService.getBoardsByMember(userId));
    }

    // GET BY CREATOR
    @GetMapping("/creator/{createdById}")
    public ResponseEntity<List<BoardDto>> getByCreator(
            @PathVariable int createdById) {
        return ResponseEntity.ok(
                boardService.getBoardsByCreator(createdById));
    }

    // UPDATE BOARD
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable int id,
            @RequestBody BoardDto boardDto) {
        try {
            return ResponseEntity.ok(
                    boardService.updateBoard(id, boardDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // CLOSE BOARD
    @PutMapping("/{id}/close")
    public ResponseEntity<String> close(
            @PathVariable int id) {
        boardService.closeBoard(id);
        return ResponseEntity.ok(
                "Board closed successfully");
    }

    // REOPEN BOARD
    @PutMapping("/{id}/reopen")
    public ResponseEntity<String> reopen(
            @PathVariable int id) {
        boardService.reopenBoard(id);
        return ResponseEntity.ok(
                "Board reopened successfully");
    }

    // DELETE BOARD
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable int id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok(
                "Board deleted successfully");
    }

    // ADD MEMBER
    @PostMapping("/{id}/members")
    public ResponseEntity<BoardMemberDto> addMember(
            @PathVariable int id,
            @RequestBody Map<String, Object> request) {
        int userId = (Integer) request.get("userId");
        String role = (String) request.get("role");
        return ResponseEntity.ok(
                boardService.addMember(id, userId, role));
    }

    // REMOVE MEMBER
    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<String> removeMember(
            @PathVariable int id,
            @PathVariable int userId) {
        boardService.removeMember(id, userId);
        return ResponseEntity.ok(
                "Member removed successfully");
    }

    // UPDATE MEMBER ROLE
    @PutMapping("/{id}/members/{userId}/role")
    public ResponseEntity<String> updateRole(
            @PathVariable int id,
            @PathVariable int userId,
            @RequestBody Map<String, String> request) {
        boardService.updateMemberRole(
                id, userId, request.get("role"));
        return ResponseEntity.ok(
                "Role updated successfully");
    }

    // GET ALL MEMBERS
    @GetMapping("/{id}/members")
    public ResponseEntity<List<BoardMemberDto>> getMembers(
            @PathVariable int id) {
        return ResponseEntity.ok(
                boardService.getMembers(id));
    }

    // GET TOTAL COUNT
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCount() {
        return ResponseEntity.ok(boardService.getTotalCount());
    }

    // GET ALL BOARDS
    @GetMapping("/all")
    public ResponseEntity<List<BoardDto>> getAll() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }
}