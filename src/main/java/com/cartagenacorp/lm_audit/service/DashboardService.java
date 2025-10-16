package com.cartagenacorp.lm_audit.service;

import com.cartagenacorp.lm_audit.dto.DashboardResponse;
import com.cartagenacorp.lm_audit.dto.NamedIdDTO;
import com.cartagenacorp.lm_audit.dto.PageResponseDTO;
import com.cartagenacorp.lm_audit.dto.StateDashboardDto;
import com.cartagenacorp.lm_audit.repository.IssueDashboardRepository;
import com.cartagenacorp.lm_audit.util.JwtContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
public class DashboardService {

    private final IssueDashboardRepository issueDashboardRepository;
    private final ConfigExternalService configExternalService;

    public DashboardService(IssueDashboardRepository issueDashboardRepository, ConfigExternalService configExternalService) {
        this.issueDashboardRepository = issueDashboardRepository;
        this.configExternalService = configExternalService;
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

    public PageResponseDTO<Map<String, Object>> getRecentIssuesBySprint(UUID projectId, UUID sprintId, int page, int size) {
        long totalElements = issueDashboardRepository.countRecentIssuesBySprint(projectId, sprintId);
        int offset = page * size;
        List<Map<String, Object>> content = issueDashboardRepository.getRecentIssuesBySprint(projectId, sprintId, size, offset);

        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageResponseDTO<>(content, totalPages, totalElements, size, page);
    }

    public PageResponseDTO<Map<String, Object>> getRecentSubtasksByIssue(UUID projectId, UUID parentId, int page, int size) {
        long totalElements = issueDashboardRepository.countRecentSubtasksByIssue(projectId, parentId);
        int offset = page * size;
        List<Map<String, Object>> content = issueDashboardRepository.getRecentSubtasksByIssue(projectId, parentId, size, offset);

        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageResponseDTO<>(content, totalPages, totalElements, size, page);
    }

//    public DashboardResponse getDashboardByProject(UUID projectId, Long finalStatusId) {
//        String token = JwtContextHolder.getToken();
//
//        List<NamedIdDTO> issueStatuses = configExternalService.getProjectConfig(token, projectId).getIssueStatuses();
//        List<Long> stateIds = issueStatuses.stream().map(NamedIdDTO::getId).toList();
//
//        Map<Long, Long> countsByState = issueDashboardRepository.countIssuesByStates(projectId, stateIds);
//        return buildDashboardResponse(issueStatuses, countsByState, projectId, finalStatusId, true);
//    }
//
//    public DashboardResponse getDashboardBySprint(UUID projectId, UUID sprintId, Long finalStatusId) {
//        String token = JwtContextHolder.getToken();
//
//        List<NamedIdDTO> issueStatuses = configExternalService.getProjectConfig(token, projectId).getIssueStatuses();
//        List<Long> stateIds = issueStatuses.stream().map(NamedIdDTO::getId).toList();
//
//        Map<Long, Long> countsByState = issueDashboardRepository.countIssuesByStatesInSprint(projectId, stateIds, sprintId);
//        return buildDashboardResponse(issueStatuses, countsByState, projectId, finalStatusId, false);
//    }
//
//    public DashboardResponse getDashboardByIssue(UUID projectId, UUID issueId, Long finalStatusId) {
//        String token = JwtContextHolder.getToken();
//
//        List<NamedIdDTO> issueStatuses = configExternalService.getProjectConfig(token, projectId).getIssueStatuses();
//        List<Long> stateIds = issueStatuses.stream().map(NamedIdDTO::getId).toList();
//
//        Map<Long, Long> countsByState = issueDashboardRepository.countIssuesByStatesAsSubtaks(projectId, stateIds, issueId);
//        return buildDashboardResponse(issueStatuses, countsByState, projectId, finalStatusId, false);
//    }
//
//
//    private DashboardResponse buildDashboardResponse(List<NamedIdDTO> statuses,
//                                                     Map<Long, Long> countsByState,
//                                                     UUID projectId,
//                                                     Long finalStatusId,
//                                                     boolean includeLists) {
//
//        long totalIssues = countsByState.values().stream().mapToLong(Long::longValue).sum();
//
//        List<NamedIdDTO> sorted = statuses.stream()
//                .sorted(Comparator.comparing(s -> Optional.ofNullable(s.getOrderIndex()).orElse(Integer.MAX_VALUE)))
//                .toList();
//
//        int finalIndex = IntStream.range(0, sorted.size())
//                .filter(i -> Objects.equals(sorted.get(i).getId(), finalStatusId))
//                .findFirst()
//                .orElse(sorted.size() - 1);
//
//        Map<Long, Double> progressById = new HashMap<>();
//        for (int i = 0; i < sorted.size(); i++) {
//            double progress = finalIndex > 0 ? (Math.min(i, finalIndex) * 100.0) / finalIndex : 100.0;
//            progressById.put(sorted.get(i).getId(), progress);
//        }
//
//        double[] totalProgress = {0.0};
//
//        List<StateDashboardDto> states = sorted.stream()
//                .map(status -> {
//                    Long count = countsByState.getOrDefault(status.getId(), 0L);
//                    double percentage = totalIssues > 0 ? (count * 100.0) / totalIssues : 0.0;
//                    double progress = progressById.getOrDefault(status.getId(), 0.0);
//                    totalProgress[0] += count * progress;
//                    return new StateDashboardDto(
//                            status.getId(),
//                            status.getName(),
//                            status.getColor(),
//                            count,
//                            percentage,
//                            progress
//                    );
//                })
//                .toList();
//
//        double overallProgress = totalIssues > 0 ? totalProgress[0] / totalIssues : 0.0;
//
//        PageResponseDTO<Map<String, Object>> recent = includeLists ? getRecentIssues(projectId, 0, 10) : null;
//        PageResponseDTO<Map<String, Object>> assigned = includeLists ? getAssignedIssues(projectId, 0, 10) : null;
//
//        return new DashboardResponse(overallProgress, states, recent, assigned);
//    }

    public DashboardResponse getDashboardByProject(UUID projectId) {
        String token = JwtContextHolder.getToken();

        List<NamedIdDTO> issueStatuses = configExternalService.getProjectConfig(token, projectId).getIssueStatuses();

        List<Long> stateIds = issueStatuses.stream()
                .map(NamedIdDTO::getId)
                .toList();

        Map<Long, Long> countsByState = issueDashboardRepository.countIssuesByStates(projectId, stateIds);

        PageResponseDTO<Map<String, Object>> recent = getRecentIssues(projectId, 0, 10);
        PageResponseDTO<Map<String, Object>> assigned = getAssignedIssues(projectId, 0, 10);

        return getDashboardResponse(issueStatuses, countsByState , recent, assigned);
    }

    public DashboardResponse getDashboardBySprint(UUID projectId, UUID sprintId) {
        String token = JwtContextHolder.getToken();

        List<NamedIdDTO> issueStatuses = configExternalService.getProjectConfig(token, projectId).getIssueStatuses();

        List<Long> stateIds = issueStatuses.stream()
                .map(NamedIdDTO::getId)
                .toList();

        Map<Long, Long> countsByState = issueDashboardRepository.countIssuesByStatesInSprint(projectId, stateIds, sprintId);
        PageResponseDTO<Map<String, Object>> recent = getRecentIssuesBySprint(projectId, sprintId, 0, 10);

        return getDashboardResponse(issueStatuses, countsByState, recent, null);
    }

    public DashboardResponse getDashboardByIssue(UUID projectId, UUID issueId){
        String token = JwtContextHolder.getToken();

        List<NamedIdDTO> issueStatuses = configExternalService.getProjectConfig(token, projectId).getIssueStatuses();

        List<Long> stateIds = issueStatuses.stream()
                .map(NamedIdDTO::getId)
                .toList();

        Map<Long, Long> countsByState = issueDashboardRepository.countIssuesByStatesAsSubtaks(projectId, stateIds, issueId);
        PageResponseDTO<Map<String, Object>> recent = getRecentSubtasksByIssue(projectId, issueId, 0, 10);

        return getDashboardResponse(issueStatuses, countsByState, recent, null);
    }

    public DashboardResponse getDashboardByRelatedIssues(UUID projectId, UUID issueId) {
        String token = JwtContextHolder.getToken();

        List<NamedIdDTO> issueStatuses = configExternalService.getProjectConfig(token, projectId).getIssueStatuses();

        List<Long> stateIds = issueStatuses.stream()
                .map(NamedIdDTO::getId)
                .toList();

        Map<Long, Long> countsByState = issueDashboardRepository.countIssuesByStatesAsRelated(projectId, stateIds, issueId);

        return getDashboardResponse(issueStatuses, countsByState, null, null);
    }

    private DashboardResponse getDashboardResponse(List<NamedIdDTO> issueStatuses, Map<Long, Long> countsByState, PageResponseDTO<Map<String, Object>> recent, PageResponseDTO<Map<String, Object>> assigned) {
        long totalIssues = countsByState.values().stream().mapToLong(Long::longValue).sum();

        List<StateDashboardDto> states = issueStatuses.stream()
                .map(status -> {
                    Long count = countsByState.getOrDefault(status.getId(), 0L);
                    double percentage = totalIssues > 0 ? (count * 100.0) / totalIssues : 0.0;
                    return new StateDashboardDto(
                            status.getId(),
                            status.getName(),
                            status.getColor(),
                            count,
                            percentage
                    );
                })
                .toList();

        return new DashboardResponse(states, recent, assigned);
    }
}
