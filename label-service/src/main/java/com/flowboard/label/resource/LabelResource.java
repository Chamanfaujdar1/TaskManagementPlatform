package com.flowboard.label.resource;

import com.flowboard.label.dto.ChecklistDto;
import com.flowboard.label.dto.ChecklistItemDto;
import com.flowboard.label.dto.LabelDto;
import com.flowboard.label.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LabelResource {

    private final LabelService labelService;

    // CREATE LABEL
    @PostMapping("/labels")
    public ResponseEntity<LabelDto> createLabel(
            @RequestBody LabelDto labelDto) {
        return ResponseEntity.ok(
                labelService.createLabel(labelDto));
    }

    // GET LABELS BY BOARD
    @GetMapping("/labels/board/{boardId}")
    public ResponseEntity<List<LabelDto>> getByBoard(
            @PathVariable int boardId) {
        return ResponseEntity.ok(
                labelService.getLabelsByBoard(boardId));
    }

    // GET LABEL BY ID
    @GetMapping("/labels/{id}")
    public ResponseEntity<LabelDto> getById(
            @PathVariable int id) {
        return ResponseEntity.ok(
                labelService.getLabelById(id));
    }

    // UPDATE LABEL
    @PutMapping("/labels/{id}")
    public ResponseEntity<LabelDto> update(
            @PathVariable int id,
            @RequestBody LabelDto labelDto) {
        return ResponseEntity.ok(
                labelService.updateLabel(id, labelDto));
    }

    // DELETE LABEL
    @DeleteMapping("/labels/{id}")
    public ResponseEntity<String> delete(
            @PathVariable int id) {
        labelService.deleteLabel(id);
        return ResponseEntity.ok(
                "Label deleted successfully");
    }

    // ADD LABEL TO CARD
    @PostMapping("/labels/{labelId}/cards/{cardId}")
    public ResponseEntity<String> addToCard(
            @PathVariable int labelId,
            @PathVariable int cardId) {
        labelService.addLabelToCard(cardId, labelId);
        return ResponseEntity.ok(
                "Label added to card successfully");
    }

    // REMOVE LABEL FROM CARD
    @DeleteMapping("/labels/{labelId}/cards/{cardId}")
    public ResponseEntity<String> removeFromCard(
            @PathVariable int labelId,
            @PathVariable int cardId) {
        labelService.removeLabelFromCard(cardId, labelId);
        return ResponseEntity.ok(
                "Label removed from card successfully");
    }

    // GET LABELS FOR CARD
    @GetMapping("/labels/card/{cardId}")
    public ResponseEntity<List<LabelDto>> getForCard(
            @PathVariable int cardId) {
        return ResponseEntity.ok(
                labelService.getLabelsForCard(cardId));
    }

    // CREATE CHECKLIST
    @PostMapping("/checklists")
    public ResponseEntity<ChecklistDto> createChecklist(
            @RequestBody ChecklistDto checklistDto) {
        return ResponseEntity.ok(
                labelService.createChecklist(checklistDto));
    }

    // GET CHECKLISTS BY CARD
    @GetMapping("/checklists/card/{cardId}")
    public ResponseEntity<List<ChecklistDto>> getChecklists(
            @PathVariable int cardId) {
        return ResponseEntity.ok(
                labelService.getChecklistsByCard(cardId));
    }

    // DELETE CHECKLIST
    @DeleteMapping("/checklists/{id}")
    public ResponseEntity<String> deleteChecklist(
            @PathVariable int id) {
        labelService.deleteChecklist(id);
        return ResponseEntity.ok(
                "Checklist deleted successfully");
    }

    // ADD CHECKLIST ITEM
    @PostMapping("/checklists/{id}/items")
    public ResponseEntity<ChecklistItemDto> addItem(
            @PathVariable int id,
            @RequestBody ChecklistItemDto itemDto) {
        itemDto.setChecklistId(id);
        return ResponseEntity.ok(
                labelService.addItem(itemDto));
    }

    // TOGGLE CHECKLIST ITEM
    @PutMapping("/checklists/items/{itemId}/toggle")
    public ResponseEntity<String> toggleItem(
            @PathVariable int itemId) {
        labelService.toggleItem(itemId);
        return ResponseEntity.ok(
                "Item toggled successfully");
    }

    // GET CHECKLIST PROGRESS
    @GetMapping("/checklists/card/{cardId}/progress")
    public ResponseEntity<Map<String, Integer>> getProgress(
            @PathVariable int cardId) {
        int progress =
                labelService.getChecklistProgress(cardId);
        return ResponseEntity.ok(
                Map.of("progress", progress));
    }
}