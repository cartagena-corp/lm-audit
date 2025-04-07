package com.cartagenacorp.lm_audit.service;

import com.cartagenacorp.lm_audit.repository.IssueDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DashboardService {

    private final IssueDashboardRepository issueDashboardRepository;

    @Autowired
    public DashboardService(IssueDashboardRepository issueDashboardRepository) {
        this.issueDashboardRepository = issueDashboardRepository;
    }

    public Map<String, Object> getDashboardByProject(UUID projectId) {
        Map<String, Object> data = new HashMap<>();
        data.put("openCount", issueDashboardRepository.countOpenIssuesByProject(projectId));
        data.put("closedCount", issueDashboardRepository.countClosedIssuesByProject(projectId));
        data.put("recentIssues", issueDashboardRepository.getRecentIssuesByProject(projectId));
        data.put("assignedIssues", issueDashboardRepository.getAssignedIssuesByProject(projectId));

        return data;
    }
}
