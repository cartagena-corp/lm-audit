package com.cartagenacorp.lm_audit.service;

import com.cartagenacorp.lm_audit.dto.IssueHistoryDtoRequest;
import com.cartagenacorp.lm_audit.dto.IssueHistoryDtoResponse;
import com.cartagenacorp.lm_audit.dto.PageResponseDTO;
import com.cartagenacorp.lm_audit.dto.UserBasicDataDto;
import com.cartagenacorp.lm_audit.entity.IssueHistory;
import com.cartagenacorp.lm_audit.mapper.IssueHistoryMapper;
import com.cartagenacorp.lm_audit.repository.IssueHistoryRepository;
import com.cartagenacorp.lm_audit.util.JwtContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class IssueHistoryService {

    private final IssueHistoryRepository historyRepository;
    private final IssueHistoryMapper issueHistoryMapper;
    private final UserExternalService userExternalService;

    public IssueHistoryService(IssueHistoryRepository historyRepository, IssueHistoryMapper issueHistoryMapper, UserExternalService userExternalService) {
        this.historyRepository = historyRepository;
        this.issueHistoryMapper = issueHistoryMapper;
        this.userExternalService = userExternalService;
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<IssueHistoryDtoResponse> getHistoryByProject(UUID projectId, Pageable pageable) {
        Page<IssueHistory> historyPage = historyRepository.findByProjectId(projectId, pageable);
        return new PageResponseDTO<>(new PageImpl<>(enrichWithUsers(historyPage), pageable, historyPage.getTotalElements()));
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<IssueHistoryDtoResponse> getHistoryByIssue(UUID issueId, Pageable pageable) {
        Page<IssueHistory> historyPage = historyRepository.findByIssueId(issueId, pageable);
        return new PageResponseDTO<>(new PageImpl<>(enrichWithUsers(historyPage), pageable, historyPage.getTotalElements()));
    }

    private List<IssueHistoryDtoResponse> enrichWithUsers(Page<IssueHistory> historyPage) {
        List<UUID> userIds = historyPage.getContent().stream()
                .map(IssueHistory::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<UserBasicDataDto> users = userExternalService.getUsersData(
                JwtContextHolder.getToken(),
                userIds.stream().map(UUID::toString).toList()
        );

        Map<UUID, UserBasicDataDto> userMap = users.stream()
                .collect(Collectors.toMap(UserBasicDataDto::getId, Function.identity()));

        return historyPage.stream()
                .map(history -> {
                    IssueHistoryDtoResponse dto = issueHistoryMapper.toDtoResponse(history);
                    dto.setUserBasicDataDto(
                            userMap.getOrDefault(history.getUserId(),
                                    new UserBasicDataDto(history.getUserId(), null, null, null, null, null, null))
                    );
                    return dto;
                }).toList();
    }

    @Transactional
    public void logChange(IssueHistoryDtoRequest issueHistoryDtoRequest) {
        IssueHistory history = new IssueHistory();
        history.setIssueId(issueHistoryDtoRequest.getIssueId());
        history.setIssueTitle(issueHistoryDtoRequest.getIssueTitle());
        history.setUserId(issueHistoryDtoRequest.getUserId());
        history.setAction(issueHistoryDtoRequest.getAction());
        history.setDescription(issueHistoryDtoRequest.getDescription());
        history.setTimestamp(LocalDateTime.now());
        history.setProjectId(issueHistoryDtoRequest.getProjectId());
        history.setBeforeChange(issueHistoryDtoRequest.getBeforeChange());
        history.setAfterChange(issueHistoryDtoRequest.getAfterChange());
        historyRepository.save(history);
    }
}

