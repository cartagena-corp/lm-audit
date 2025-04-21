package com.cartagenacorp.lm_audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private Map<Long, Long> countsByState;
    private PageResponseDTO<Map<String, Object>> recentIssues;
    private PageResponseDTO<Map<String, Object>> assignedIssues;
}
