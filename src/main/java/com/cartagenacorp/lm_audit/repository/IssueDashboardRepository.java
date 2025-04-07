package com.cartagenacorp.lm_audit.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class IssueDashboardRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long countOpenIssuesByProject(UUID projectId) {
        String sql = "SELECT COUNT(*) FROM issue WHERE status = 'OPEN' AND project_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, projectId);
    }

    public long countClosedIssuesByProject(UUID projectId) {
        String sql = "SELECT COUNT(*) FROM issue WHERE status = 'CLOSED' AND project_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, projectId);
    }

    public List<Map<String, Object>> getRecentIssuesByProject(UUID projectId) {
        String sql = "SELECT id, title, created_at FROM issue WHERE project_id = ? ORDER BY created_at DESC LIMIT 5";
        return jdbcTemplate.queryForList(sql, projectId);
    }

    public List<Map<String, Object>> getAssignedIssuesByProject(UUID projectId) {
        String sql = "SELECT id, title, assigned_id FROM issue WHERE assigned_id IS NOT NULL AND project_id = ?";
        return jdbcTemplate.queryForList(sql, projectId);
    }
}

