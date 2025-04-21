package com.cartagenacorp.lm_audit.service;

import com.cartagenacorp.lm_audit.dto.DashboardResponse;
import com.cartagenacorp.lm_audit.dto.PageResponseDTO;
import com.cartagenacorp.lm_audit.repository.IssueDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public DashboardResponse getDashboardByProject(UUID projectId, List<Long> states) {
        Map<Long, Long> countsByState = issueDashboardRepository.countIssuesByStates(projectId, states);
        PageResponseDTO<Map<String, Object>> recent = getRecentIssues(projectId, 0, 10);
        PageResponseDTO<Map<String, Object>> assigned = getAssignedIssues(projectId, 0, 10);

        return new DashboardResponse(countsByState, recent, assigned);
    }
}
