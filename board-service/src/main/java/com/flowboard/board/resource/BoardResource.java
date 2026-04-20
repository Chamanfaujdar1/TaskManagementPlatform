package com.flowboard.board.resource;

import com.flowboard.board.entity.Board;
import com.flowboard.board.entity.BoardMember;
import com.flowboard.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/boards")
public class BoardResource {

    @Autowired
    private BoardService boardService;

    // CREATE BOARD
    @PostMapping
    public ResponseEntity<Board> create(
            @RequestBody Board board) {
        return ResponseEntity.ok(
                boardService.createBoard(board));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(
            @PathVariable int id) {
        return ResponseEntity.ok(
                boardService.getBoardById(id));
    }

    // GET BY WORKSPACE
    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<Board>> getByWorkspace(
            @PathVariable int workspaceId) {
        return ResponseEntity.ok(
                boardService.getBoardsByWorkspace(workspaceId));
    }

    // GET BY MEMBER
    @GetMapping("/member/{userId}")
    public ResponseEntity<List<Board>> getByMember(
            @PathVariable int userId) {
        return ResponseEntity.ok(
                boardService.getBoardsByMember(userId));
    }

    // GET BY CREATOR
    @GetMapping("/creator/{createdById}")
    public ResponseEntity<List<Board>> getByCreator(
            @PathVariable int createdById) {
        return ResponseEntity.ok(
                boardService.getBoardsByCreator(createdById));
    }

    // UPDATE BOARD
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable int id,
            @RequestBody Board board) {
        try {
            return ResponseEntity.ok(
                    boardService.updateBoard(id, board));
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
    public ResponseEntity<BoardMember> addMember(
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
    public ResponseEntity<List<BoardMember>> getMembers(
            @PathVariable int id) {
        return ResponseEntity.ok(
                boardService.getMembers(id));
    }
}