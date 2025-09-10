package com.cartagenacorp.lm_audit.service;

import com.cartagenacorp.lm_audit.dto.IssueHistoryDTO;
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
    public PageResponseDTO<IssueHistoryDTO> getHistoryByProject(UUID projectId, Pageable pageable) {
        Page<IssueHistory> historyPage = historyRepository.findByProjectId(projectId, pageable);
        return new PageResponseDTO<>(new PageImpl<>(enrichWithUsers(historyPage), pageable, historyPage.getTotalElements()));
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<IssueHistoryDTO> getHistoryByIssue(UUID issueId, Pageable pageable) {
        Page<IssueHistory> historyPage = historyRepository.findByIssueId(issueId, pageable);
        return new PageResponseDTO<>(new PageImpl<>(enrichWithUsers(historyPage), pageable, historyPage.getTotalElements()));
    }

    private List<IssueHistoryDTO> enrichWithUsers(Page<IssueHistory> historyPage) {
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
                    IssueHistoryDTO dto = issueHistoryMapper.toDto(history);
                    dto.setUserBasicDataDto(
                            userMap.getOrDefault(history.getUserId(),
                                    new UserBasicDataDto(history.getUserId(), null, null, null, null, null, null))
                    );
                    return dto;
                }).toList();
    }

    @Transactional
    public void logChange(UUID issueId, UUID userId, String action, String description, UUID projectId) {
        IssueHistory history = new IssueHistory();
        history.setIssueId(issueId);
        history.setUserId(userId);
        history.setAction(action);
        history.setDescription(description);
        history.setTimestamp(LocalDateTime.now());
        history.setProjectId(projectId);
        historyRepository.save(history);
    }
}

