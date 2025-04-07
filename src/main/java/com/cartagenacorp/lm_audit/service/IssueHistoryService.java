package com.cartagenacorp.lm_audit.service;

import com.cartagenacorp.lm_audit.entity.IssueHistory;
import com.cartagenacorp.lm_audit.repository.IssueHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class IssueHistoryService {

    private final IssueHistoryRepository historyRepository;

    @Autowired
    public IssueHistoryService(IssueHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Transactional(readOnly = true)
    public Page<IssueHistory> getAllHistoryByProject(UUID projectId, Pageable pageable){
        return historyRepository.findAllByProjectId(projectId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<IssueHistory> getHistoryByIssue(UUID issueId, Pageable pageable) {
        return historyRepository.findByIssueId(issueId, pageable);
    }

    @Transactional
    public void logChange(UUID issueId, UUID userId, String action, String description, UUID projectId) {
        IssueHistory history = new IssueHistory();
        history.setIssueId(issueId);
        history.setUserId(userId);
        history.setAction(action);
        history.setDescription(description);
        history.setTimestamp(LocalDateTime.now());
        history.setProjectId(projectId);
        historyRepository.save(history);
    }
}

