package com.cartagenacorp.lm_audit.repository;

import com.cartagenacorp.lm_audit.entity.IssueHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IssueHistoryRepository extends JpaRepository<IssueHistory, UUID> {
    Page<IssueHistory> findByIssueId(UUID issueId, Pageable pageable);

    Page<IssueHistory> findAllByProjectId(UUID projectId, Pageable pageable);
}

