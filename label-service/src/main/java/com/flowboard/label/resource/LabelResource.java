package com.flowboard.label.resource;

import com.flowboard.label.entity.Checklist;
import com.flowboard.label.entity.ChecklistItem;
import com.flowboard.label.entity.Label;
import com.flowboard.label.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class LabelResource {

    @Autowired
    private LabelService labelService;

    // CREATE LABEL
    @PostMapping("/labels")
    public ResponseEntity<Label> createLabel(
            @RequestBody Label label) {
        return ResponseEntity.ok(
                labelService.createLabel(label));
    }

    // GET LABELS BY BOARD
    @GetMapping("/labels/board/{boardId}")
    public ResponseEntity<List<Label>> getByBoard(
            @PathVariable int boardId) {
        return ResponseEntity.ok(
                labelService.getLabelsByBoard(boardId));
    }

    // GET LABEL BY ID
    @GetMapping("/labels/{id}")
    public ResponseEntity<Label> getById(
            @PathVariable int id) {
        return ResponseEntity.ok(
                labelService.getLabelById(id));
    }

    // UPDATE LABEL
    @PutMapping("/labels/{id}")
    public ResponseEntity<Label> update(
            @PathVariable int id,
            @RequestBody Label label) {
        return ResponseEntity.ok(
                labelService.updateLabel(id, label));
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
    public ResponseEntity<List<Label>> getForCard(
            @PathVariable int cardId) {
        return ResponseEntity.ok(
                labelService.getLabelsForCard(cardId));
    }

    // CREATE CHECKLIST
    @PostMapping("/checklists")
    public ResponseEntity<Checklist> createChecklist(
            @RequestBody Checklist checklist) {
        return ResponseEntity.ok(
                labelService.createChecklist(checklist));
    }

    // GET CHECKLISTS BY CARD
    @GetMapping("/checklists/card/{cardId}")
    public ResponseEntity<List<Checklist>> getChecklists(
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
    public ResponseEntity<ChecklistItem> addItem(
            @PathVariable int id,
            @RequestBody ChecklistItem item) {
        item.setChecklistId(id);
        return ResponseEntity.ok(
                labelService.addItem(item));
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