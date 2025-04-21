package com.cartagenacorp.lm_audit.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class IssueDashboardRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Map<Long, Long> countIssuesByStates(UUID projectId, List<Long> states) {
        String sql = """
            SELECT status, COUNT(*) as count
            FROM issue
            WHERE project_id = :projectId AND status IN (:states)
            GROUP BY status
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("states", states);

        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sql, params);

        Map<Long, Long> countsByState = new HashMap<>();
        for (Map<String, Object> row : results) {
            Long status = ((Number) row.get("status")).longValue();
            Long count = ((Number) row.get("count")).longValue();
            countsByState.put(status, count);
        }

        for (Long state : states) {
            countsByState.putIfAbsent(state, 0L);
        }

        return countsByState;
    }

    public List<Map<String, Object>> getRecentIssuesByProject(UUID projectId, int limit, int offset) {
        String sql = "SELECT id, title, created_at FROM issue WHERE project_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.queryForList(sql, projectId, limit, offset);
    }

    public long countRecentIssuesByProject(UUID projectId) {
        String sql = "SELECT COUNT(*) FROM issue WHERE project_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, projectId);
    }

    public List<Map<String, Object>> getAssignedIssuesByProject(UUID projectId, int limit, int offset) {
        String sql = "SELECT id, title, assigned_id FROM issue WHERE assigned_id IS NOT NULL AND project_id = ? LIMIT ? OFFSET ?";
        return jdbcTemplate.queryForList(sql, projectId, limit, offset);
    }

    public long countAssignedIssuesByProject(UUID projectId) {
        String sql = "SELECT COUNT(*) FROM issue WHERE assigned_id IS NOT NULL AND project_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, projectId);
    }
}

