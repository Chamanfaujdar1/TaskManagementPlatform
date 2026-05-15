package com.flowboard.workspace.resource;

import com.flowboard.workspace.entity.Workspace;
import com.flowboard.workspace.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportResource {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @GetMapping("/activity")
    public ResponseEntity<List<Map<String, Object>>> getActivityReport(
            @RequestParam(required = false) Integer workspaceId) {
        
        List<Map<String, Object>> report = new ArrayList<>();
        
        if (workspaceId != null) {
            workspaceRepository.findById(workspaceId).ifPresent(w -> {
                report.add(createActivityEntry(w));
            });
        } else {
            workspaceRepository.findAll().forEach(w -> {
                report.add(createActivityEntry(w));
            });
        }
        
        return ResponseEntity.ok(report);
    }

    private Map<String, Object> createActivityEntry(Workspace w) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("workspaceId", w.getWorkspaceId());
        entry.put("workspaceName", w.getName());
        entry.put("ownerId", w.getOwnerId());
        entry.put("createdAt", w.getCreatedAt());
        // In a real app, we would join with boards/cards to get real counts
        entry.put("activityScore", Math.floor(Math.random() * 100)); 
        return entry;
    }
}
