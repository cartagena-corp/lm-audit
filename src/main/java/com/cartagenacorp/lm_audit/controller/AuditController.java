package com.cartagenacorp.lm_audit.controller;

import com.cartagenacorp.lm_audit.dto.IssueHistoryDtoRequest;
import com.cartagenacorp.lm_audit.dto.IssueHistoryDtoResponse;
import com.cartagenacorp.lm_audit.dto.PageResponseDTO;
import com.cartagenacorp.lm_audit.service.DashboardService;
import com.cartagenacorp.lm_audit.service.IssueHistoryService;
import com.cartagenacorp.lm_audit.util.RequiresAuthentication;
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
    public ResponseEntity<PageResponseDTO<IssueHistoryDtoResponse>> getAllHistoryByProject(
            @PathVariable String projectId,
            @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID uuid = UUID.fromString(projectId);
        return ResponseEntity.ok(issueHistoryService.getHistoryByProject(uuid, pageable));
    }

    @GetMapping("/allByIssue/{issueId}")
    @RequiresPermission({"AUDIT_READ"})
    public ResponseEntity<PageResponseDTO<IssueHistoryDtoResponse>> getHistoryByIssue(
            @PathVariable String issueId,
            @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID uuid = UUID.fromString(issueId);
        return ResponseEntity.ok(issueHistoryService.getHistoryByIssue(uuid, pageable));
    }

    @PostMapping("/logChange") //se usa desde lm-issues (uso interno)
    @RequiresAuthentication
    public ResponseEntity<Void> logChange(@RequestBody IssueHistoryDtoRequest issueHistoryDtoRequest) {
        issueHistoryService.logChange(issueHistoryDtoRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/dashboard")
    @RequiresPermission({"AUDIT_READ"})
    public ResponseEntity<?> getDashboardByProject(@PathVariable String projectId, @RequestParam(required = false) Long finalStatusId) {
        UUID uuid = UUID.fromString(projectId);
        return ResponseEntity.ok(dashboardService.getDashboardByProject(uuid));
    }

    @GetMapping("/{projectId}/sprint/{sprintId}/dashboard")
    @RequiresPermission({"AUDIT_READ"})
    public ResponseEntity<?> getDashboardByProjectAndSprint(@PathVariable String projectId, @PathVariable String sprintId, @RequestParam(required = false) Long finalStatusId) {
        UUID projectUuid = UUID.fromString(projectId);
        UUID sprintUuid = UUID.fromString(sprintId);
        return ResponseEntity.ok(dashboardService.getDashboardBySprint(projectUuid, sprintUuid));
    }

    @GetMapping("/{projectId}/issue/{issueId}/dashboard")
    @RequiresPermission({"AUDIT_READ"})
    public ResponseEntity<?> getDashboardByProjectAndIssue(@PathVariable String projectId, @PathVariable String issueId, @RequestParam(required = false) Long finalStatusId) {
        UUID projectUuid = UUID.fromString(projectId);
        UUID issueUuid = UUID.fromString(issueId);
        return ResponseEntity.ok(dashboardService.getDashboardByIssue(projectUuid, issueUuid));
    }

    @GetMapping("/{projectId}/relatedIssues/{issueId}/dashboard")
    @RequiresPermission({"AUDIT_READ"})
    public ResponseEntity<?> getDashboardByProjectAndRelatedIssues(@PathVariable String projectId, @PathVariable String issueId, @RequestParam(required = false) Long finalStatusId) {
        UUID projectUuid = UUID.fromString(projectId);
        UUID issueUuid = UUID.fromString(issueId);
        return ResponseEntity.ok(dashboardService.getDashboardByRelatedIssues(projectUuid, issueUuid));
    }
}
