package com.flowboard.workspace.resource;

import com.flowboard.workspace.dto.WorkspaceDto;
import com.flowboard.workspace.dto.WorkspaceMemberDto;
import com.flowboard.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces")
public class WorkspaceResource {

    private final WorkspaceService workspaceService;

    // CREATE WORKSPACE
    @PostMapping
    public ResponseEntity<WorkspaceDto> create(
            @RequestBody WorkspaceDto workspaceDto) {
        return ResponseEntity.ok(
                workspaceService.createWorkspace(workspaceDto));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDto> getById(
            @PathVariable int id) {
        return ResponseEntity.ok(
                workspaceService.getById(id));
    }

    // GET BY OWNER
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<WorkspaceDto>> getByOwner(
            @PathVariable int ownerId) {
        return ResponseEntity.ok(
                workspaceService.getByOwner(ownerId));
    }

    // GET BY MEMBER
    @GetMapping("/member/{userId}")
    public ResponseEntity<List<WorkspaceDto>> getByMember(
            @PathVariable int userId) {
        return ResponseEntity.ok(
                workspaceService.getByMember(userId));
    }

    // GET PUBLIC WORKSPACES
    @GetMapping("/public")
    public ResponseEntity<List<WorkspaceDto>> getPublic() {
        return ResponseEntity.ok(
                workspaceService.getPublicWorkspaces());
    }

    // UPDATE WORKSPACE
    @PutMapping("/{id}")
    public ResponseEntity<WorkspaceDto> update(
            @PathVariable int id,
            @RequestBody WorkspaceDto workspaceDto) {
        return ResponseEntity.ok(
                workspaceService.updateWorkspace(id, workspaceDto));
    }

    // DELETE WORKSPACE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable int id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.ok(
                "Workspace deleted successfully");
    }

    // ADD MEMBER
    @PostMapping("/{id}/members")
    public ResponseEntity<WorkspaceMemberDto> addMember(
            @PathVariable int id,
            @RequestBody Map<String, Object> request) {
        int userId = (Integer) request.get("userId");
        String role = (String) request.get("role");
        return ResponseEntity.ok(
                workspaceService.addMember(id, userId, role));
    }

    // REMOVE MEMBER
    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<String> removeMember(
            @PathVariable int id,
            @PathVariable int userId) {
        workspaceService.removeMember(id, userId);
        return ResponseEntity.ok(
                "Member removed successfully");
    }

    // UPDATE MEMBER ROLE
    @PutMapping("/{id}/members/{userId}/role")
    public ResponseEntity<String> updateRole(
            @PathVariable int id,
            @PathVariable int userId,
            @RequestBody Map<String, String> request) {
        workspaceService.updateMemberRole(
                id, userId, request.get("role"));
        return ResponseEntity.ok(
                "Role updated successfully");
    }

    // GET ALL MEMBERS
    @GetMapping("/{id}/members")
    public ResponseEntity<List<WorkspaceMemberDto>> getMembers(
            @PathVariable int id) {
        return ResponseEntity.ok(
                workspaceService.getMembers(id));
    }

    // GET TOTAL COUNT
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCount() {
        return ResponseEntity.ok(workspaceService.getTotalCount());
    }

    // GET ALL WORKSPACES
    @GetMapping("/all")
    public ResponseEntity<List<WorkspaceDto>> getAll() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }
}