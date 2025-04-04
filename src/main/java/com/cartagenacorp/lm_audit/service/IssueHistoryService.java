package com.cartagenacorp.lm_audit.service;

import com.cartagenacorp.lm_audit.entity.IssueHistory;
import com.cartagenacorp.lm_audit.repository.IssueHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class IssueHistoryService {

    private final IssueHistoryRepository historyRepository;

    @Autowired
    public IssueHistoryService(IssueHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Transactional(readOnly = true)
    public List<IssueHistory> getAllHistory(){
        return historyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<IssueHistory> getHistoryByIssue(UUID issueId) {
        return historyRepository.findByIssueId(issueId);
    }

    @Transactional
    public void logChange(UUID issueId, UUID userId, String action, String description) {
        IssueHistory history = new IssueHistory();
        history.setIssueId(issueId);
        history.setUserId(userId);
        history.setAction(action);
        history.setDescription(description);
        history.setTimestamp(LocalDateTime.now());
        historyRepository.save(history);
    }
}

