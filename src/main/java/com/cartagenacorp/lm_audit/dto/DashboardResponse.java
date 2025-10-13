package com.cartagenacorp.lm_audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    //private Double overallProgress;
    private List<StateDashboardDto> states;
    private PageResponseDTO<Map<String, Object>> recentIssues;
    private PageResponseDTO<Map<String, Object>> assignedIssues;
}
