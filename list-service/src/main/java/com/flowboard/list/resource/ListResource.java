package com.flowboard.list.resource;

import com.flowboard.list.dto.TaskListDto;
import com.flowboard.list.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lists")
public class ListResource {

    private final ListService listService;

    // CREATE LIST
    @PostMapping
    public ResponseEntity<TaskListDto> create(
            @RequestBody TaskListDto taskListDto) {
        return ResponseEntity.ok(
                listService.createList(taskListDto));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskListDto> getById(
            @PathVariable int id) {
        return ResponseEntity.ok(
                listService.getListById(id));
    }

    // GET BY BOARD
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<TaskListDto>> getByBoard(
            @PathVariable int boardId) {
        return ResponseEntity.ok(
                listService.getListsByBoard(boardId));
    }

    // GET ARCHIVED LISTS
    @GetMapping("/board/{boardId}/archived")
    public ResponseEntity<List<TaskListDto>> getArchived(
            @PathVariable int boardId) {
        return ResponseEntity.ok(
                listService.getArchivedLists(boardId));
    }

    // UPDATE LIST
    @PutMapping("/{id}")
    public ResponseEntity<TaskListDto> update(
            @PathVariable int id,
            @RequestBody TaskListDto taskListDto) {
        return ResponseEntity.ok(
                listService.updateList(id, taskListDto));
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
    public ResponseEntity<TaskListDto> move(
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