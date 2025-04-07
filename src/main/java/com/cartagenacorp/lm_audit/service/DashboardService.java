package com.cartagenacorp.lm_audit.service;

import com.cartagenacorp.lm_audit.dto.PageResponseDTO;
import com.cartagenacorp.lm_audit.repository.IssueDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DashboardService {

    private final IssueDashboardRepository issueDashboardRepository;

    @Autowired
    public DashboardService(IssueDashboardRepository issueDashboardRepository) {
        this.issueDashboardRepository = issueDashboardRepository;
    }

    public PageResponseDTO<Map<String, Object>> getRecentIssues(UUID projectId, int page, int size) {
        long totalElements = issueDashboardRepository.countRecentIssuesByProject(projectId);
        int offset = page * size;
        List<Map<String, Object>> content = issueDashboardRepository.getRecentIssuesByProject(projectId, size, offset);

        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageResponseDTO<>(content, totalPages, totalElements, size, page);
    }

    public PageResponseDTO<Map<String, Object>> getAssignedIssues(UUID projectId, int page, int size) {
        long totalElements = issueDashboardRepository.countAssignedIssuesByProject(projectId);
        int offset = page * size;
        List<Map<String, Object>> content = issueDashboardRepository.getAssignedIssuesByProject(projectId, size, offset);

        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageResponseDTO<>(content, totalPages, totalElements, size, page);
    }

    public Map<String, Object> getDashboardByProject(UUID projectId) {
        Map<String, Object> data = new HashMap<>();
        data.put("openCount", issueDashboardRepository.countOpenIssuesByProject(projectId));
        data.put("closedCount", issueDashboardRepository.countClosedIssuesByProject(projectId));

        return data;
    }
}
