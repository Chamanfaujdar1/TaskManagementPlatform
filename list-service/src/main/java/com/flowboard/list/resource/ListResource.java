package com.flowboard.list.resource;

import com.flowboard.list.entity.TaskList;
import com.flowboard.list.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/lists")
public class ListResource {

    @Autowired
    private ListService listService;

    // CREATE LIST
    @PostMapping
    public ResponseEntity<TaskList> create(
            @RequestBody TaskList taskList) {
        return ResponseEntity.ok(
                listService.createList(taskList));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskList> getById(
            @PathVariable int id) {
        return ResponseEntity.ok(
                listService.getListById(id));
    }

    // GET BY BOARD
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<TaskList>> getByBoard(
            @PathVariable int boardId) {
        return ResponseEntity.ok(
                listService.getListsByBoard(boardId));
    }

    // GET ARCHIVED LISTS
    @GetMapping("/board/{boardId}/archived")
    public ResponseEntity<List<TaskList>> getArchived(
            @PathVariable int boardId) {
        return ResponseEntity.ok(
                listService.getArchivedLists(boardId));
    }

    // UPDATE LIST
    @PutMapping("/{id}")
    public ResponseEntity<TaskList> update(
            @PathVariable int id,
            @RequestBody TaskList taskList) {
        return ResponseEntity.ok(
                listService.updateList(id, taskList));
    }

    // REORDER LISTS
    @PutMapping("/reorder")
    public ResponseEntity<String> reorder(
            @RequestBody Map<String, Object> request) {
        int boardId = (Integer) request.get("boardId");
        List<Integer> orderedListIds =
                (List<Integer>) request.get("orderedListIds");
        listService.reorderLists(boardId, orderedListIds);
        return ResponseEntity.ok(
                "Lists reordered successfully");
    }

    // ARCHIVE LIST
    @PostMapping("/{id}/archive")
    public ResponseEntity<String> archive(
            @PathVariable int id) {
        listService.archiveList(id);
        return ResponseEntity.ok(
                "List archived successfully");
    }

    // UNARCHIVE LIST
    @PostMapping("/{id}/unarchive")
    public ResponseEntity<String> unarchive(
            @PathVariable int id) {
        listService.unarchiveList(id);
        return ResponseEntity.ok(
                "List unarchived successfully");
    }

    // MOVE LIST TO ANOTHER BOARD
    @PutMapping("/{id}/move")
    public ResponseEntity<TaskList> move(
            @PathVariable int id,
            @RequestBody Map<String, Integer> request) {
        int targetBoardId = request.get("targetBoardId");
        return ResponseEntity.ok(
                listService.moveList(id, targetBoardId));
    }

    // DELETE LIST
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable int id) {
        listService.deleteList(id);
        return ResponseEntity.ok(
                "List deleted successfully");
    }
}