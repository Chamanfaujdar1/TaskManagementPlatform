package com.flowboard.card.resource;

import com.flowboard.card.dto.CardDto;
import com.flowboard.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
public class CardResource {

    private final CardService cardService;

    // CREATE CARD
    @PostMapping
    public ResponseEntity<CardDto> create(
            @RequestBody CardDto cardDto) {
        return ResponseEntity.ok(
                cardService.createCard(cardDto));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getById(
            @PathVariable int id) {
        return ResponseEntity.ok(
                cardService.getCardById(id));
    }

    // GET BY LIST
    @GetMapping("/list/{listId}")
    public ResponseEntity<List<CardDto>> getByList(
            @PathVariable int listId) {
        return ResponseEntity.ok(
                cardService.getCardsByList(listId));
    }

    // GET BY BOARD
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<CardDto>> getByBoard(
            @PathVariable int boardId) {
        return ResponseEntity.ok(
                cardService.getCardsByBoard(boardId));
    }

    // GET BY ASSIGNEE
    @GetMapping("/assignee/{userId}")
    public ResponseEntity<List<CardDto>> getByAssignee(
            @PathVariable int userId) {
        return ResponseEntity.ok(
                cardService.getCardsByAssignee(userId));
    }

    // GET OVERDUE CARDS
    @GetMapping("/overdue")
    public ResponseEntity<List<CardDto>> getOverdue() {
        return ResponseEntity.ok(
                cardService.getOverdueCards());
    }

    // SEARCH CARDS
    @GetMapping("/search")
    public ResponseEntity<List<CardDto>> search(
            @RequestParam String q) {
        return ResponseEntity.ok(
                cardService.searchCards(q));
    }

    // UPDATE CARD
    @PutMapping("/{id}")
    public ResponseEntity<CardDto> update(
            @PathVariable int id,
            @RequestBody CardDto cardDto) {
        return ResponseEntity.ok(
                cardService.updateCard(id, cardDto));
    }

    // MOVE CARD
    @PutMapping("/{id}/move")
    public ResponseEntity<CardDto> move(
            @PathVariable int id,
            @RequestBody Map<String, Integer> request) {
        int targetListId = request.get("targetListId");
        int newPosition = request.get("newPosition");
        return ResponseEntity.ok(
                cardService.moveCard(id, targetListId,
                        newPosition));
    }

    // REORDER CARDS
    @PutMapping("/reorder")
    public ResponseEntity<String> reorder(
            @RequestBody Map<String, Object> request) {
        int listId = (Integer) request.get("listId");
        List<Integer> orderedCardIds =
                (List<Integer>) request.get("orderedCardIds");
        cardService.reorderCards(listId, orderedCardIds);
        return ResponseEntity.ok(
                "Cards reordered successfully");
    }

    // SET ASSIGNEE
    @PutMapping("/{id}/assignee")
    public ResponseEntity<CardDto> setAssignee(
            @PathVariable int id,
            @RequestBody Map<String, Integer> request) {
        return ResponseEntity.ok(
                cardService.setAssignee(
                        id, request.get("assigneeId")));
    }

    // SET PRIORITY
    @PutMapping("/{id}/priority")
    public ResponseEntity<CardDto> setPriority(
            @PathVariable int id,
            @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(
                cardService.setPriority(
                        id, request.get("priority")));
    }

    // SET STATUS
    @PutMapping("/{id}/status")
    public ResponseEntity<CardDto> setStatus(
            @PathVariable int id,
            @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(
                cardService.setStatus(
                        id, request.get("status")));
    }

    // ARCHIVE CARD
    @PostMapping("/{id}/archive")
    public ResponseEntity<String> archive(
            @PathVariable int id) {
        cardService.archiveCard(id);
        return ResponseEntity.ok(
                "Card archived successfully");
    }

    // UNARCHIVE CARD
    @PostMapping("/{id}/unarchive")
    public ResponseEntity<String> unarchive(
            @PathVariable int id) {
        cardService.unarchiveCard(id);
        return ResponseEntity.ok(
                "Card unarchived successfully");
    }

    // DELETE CARD
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable int id) {
        cardService.deleteCard(id);
        return ResponseEntity.ok(
                "Card deleted successfully");
    }

    // GET ALL CARDS
    @GetMapping("/all")
    public ResponseEntity<List<CardDto>> getAll() {
        return ResponseEntity.ok(cardService.getAllCards());
    }
}