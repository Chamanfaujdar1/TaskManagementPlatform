package com.flowboard.workspace.resource;

import com.flowboard.workspace.entity.Workspace;
import com.flowboard.workspace.entity.WorkspaceMember;
import com.flowboard.workspace.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceResource {

    @Autowired
    private WorkspaceService workspaceService;

    // CREATE WORKSPACE
    @PostMapping
    public ResponseEntity<Workspace> create(
            @RequestBody Workspace workspace) {
        return ResponseEntity.ok(
                workspaceService.createWorkspace(workspace));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Workspace> getById(
            @PathVariable int id) {
        return ResponseEntity.ok(
                workspaceService.getById(id));
    }

    // GET BY OWNER
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Workspace>> getByOwner(
            @PathVariable int ownerId) {
        return ResponseEntity.ok(
                workspaceService.getByOwner(ownerId));
    }

    // GET BY MEMBER
    @GetMapping("/member/{userId}")
    public ResponseEntity<List<Workspace>> getByMember(
            @PathVariable int userId) {
        return ResponseEntity.ok(
                workspaceService.getByMember(userId));
    }

    // GET PUBLIC WORKSPACES
    @GetMapping("/public")
    public ResponseEntity<List<Workspace>> getPublic() {
        return ResponseEntity.ok(
                workspaceService.getPublicWorkspaces());
    }

    // UPDATE WORKSPACE
    @PutMapping("/{id}")
    public ResponseEntity<Workspace> update(
            @PathVariable int id,
            @RequestBody Workspace workspace) {
        return ResponseEntity.ok(
                workspaceService.updateWorkspace(id, workspace));
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
    public ResponseEntity<WorkspaceMember> addMember(
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
    public ResponseEntity<List<WorkspaceMember>> getMembers(
            @PathVariable int id) {
        return ResponseEntity.ok(
                workspaceService.getMembers(id));
    }
}