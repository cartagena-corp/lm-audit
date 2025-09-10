package com.cartagenacorp.lm_audit.controller;

import com.cartagenacorp.lm_audit.dto.DashboardRequest;
import com.cartagenacorp.lm_audit.dto.IssueHistoryDTO;
import com.cartagenacorp.lm_audit.dto.PageResponseDTO;
import com.cartagenacorp.lm_audit.service.DashboardService;
import com.cartagenacorp.lm_audit.service.IssueHistoryService;
import com.cartagenacorp.lm_audit.util.RequiresPermission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final IssueHistoryService issueHistoryService;
    private final DashboardService dashboardService;

    public AuditController(IssueHistoryService issueHistoryService, DashboardService dashboardService) {
        this.issueHistoryService = issueHistoryService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/allByProject/{projectId}")
    @RequiresPermission({"AUDIT_READ"})
    public ResponseEntity<PageResponseDTO<IssueHistoryDTO>> getAllHistoryByProject(
            @PathVariable String projectId,
            @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID uuid = UUID.fromString(projectId);
        return ResponseEntity.ok(issueHistoryService.getHistoryByProject(uuid, pageable));
    }

    @GetMapping("/allByIssue/{issueId}")
    @RequiresPermission({"AUDIT_READ"})
    public ResponseEntity<PageResponseDTO<IssueHistoryDTO>> getHistoryByIssue(
            @PathVariable String issueId,
            @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID uuid = UUID.fromString(issueId);
        return ResponseEntity.ok(issueHistoryService.getHistoryByIssue(uuid, pageable));
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

    @PostMapping("/dashboard")
    @RequiresPermission({"AUDIT_READ"})
    public ResponseEntity<?> getDashboardByProject(@RequestBody DashboardRequest dashboardRequest) {
        return ResponseEntity.ok(
                dashboardService.getDashboardByProject(
                        dashboardRequest.getProjectId(),
                        dashboardRequest.getStates())
        );
    }
}
