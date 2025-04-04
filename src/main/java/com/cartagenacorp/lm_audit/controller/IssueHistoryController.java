package com.cartagenacorp.lm_audit.controller;

import com.cartagenacorp.lm_audit.dto.IssueHistoryDTO;
import com.cartagenacorp.lm_audit.entity.IssueHistory;
import com.cartagenacorp.lm_audit.service.IssueHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit")
public class IssueHistoryController {

    private final IssueHistoryService issueHistoryService;

    @Autowired
    public IssueHistoryController(IssueHistoryService issueHistoryService) {
        this.issueHistoryService = issueHistoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<IssueHistory>> getAllHistory() {
        List<IssueHistory> history = issueHistoryService.getAllHistory();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<List<IssueHistory>> getHistoryByIssue(@PathVariable UUID issueId) {
        List<IssueHistory> history = issueHistoryService.getHistoryByIssue(issueId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/logChange")
    public ResponseEntity<Void> logChange(@RequestBody IssueHistoryDTO issueHistoryDTO) {
        issueHistoryService.logChange(
                issueHistoryDTO.getIssueId(),
                issueHistoryDTO.getUserId(),
                issueHistoryDTO.getAction(),
                issueHistoryDTO.getDescription()
        );
        return ResponseEntity.ok().build();
    }
}
