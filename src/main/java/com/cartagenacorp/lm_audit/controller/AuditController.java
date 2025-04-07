package com.cartagenacorp.lm_audit.controller;

import com.cartagenacorp.lm_audit.dto.IssueHistoryDTO;
import com.cartagenacorp.lm_audit.entity.IssueHistory;
import com.cartagenacorp.lm_audit.service.DashboardService;
import com.cartagenacorp.lm_audit.service.IssueHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final IssueHistoryService issueHistoryService;
    private final DashboardService dashboardService;

    @Autowired
    public AuditController(IssueHistoryService issueHistoryService, DashboardService dashboardService) {
        this.issueHistoryService = issueHistoryService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/allByProject/{projectId}")
    public ResponseEntity<?> getAllHistoryByProject(@PathVariable String projectId) {
        try {
            UUID uuid = UUID.fromString(projectId);
            List<IssueHistory> history = issueHistoryService.getAllHistoryByProject(uuid);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving history: " + e.getMessage());
        }
    }

    @GetMapping("/allByIssue/{issueId}")
    public ResponseEntity<?> getHistoryByIssue(@PathVariable String issueId) {
        try {
            UUID uuid = UUID.fromString(issueId);
            List<IssueHistory> history = issueHistoryService.getHistoryByIssue(uuid);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving history: " + e.getMessage());
        }
    }

    @PostMapping("/logChange")
    public ResponseEntity<Void> logChange(@RequestBody IssueHistoryDTO issueHistoryDTO) {
        issueHistoryService.logChange(
                issueHistoryDTO.getIssueId(),
                issueHistoryDTO.getUserId(),
                issueHistoryDTO.getAction(),
                issueHistoryDTO.getDescription(),
                issueHistoryDTO.getProjectId()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dashboard/{projectId}")
    public ResponseEntity<?> getDashboardByProject(@PathVariable String projectId) {
        try {
            UUID uuid = UUID.fromString(projectId);
            return ResponseEntity.ok(dashboardService.getDashboardByProject(uuid));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving dashboard: " + e.getMessage());
        }
    }
}
