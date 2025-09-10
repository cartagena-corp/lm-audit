package com.cartagenacorp.lm_audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueHistoryDTO {
    private UUID id;
    private UUID issueId;
    private UUID userId;
    private String action;
    private String description;
    private LocalDateTime timestamp;
    private UUID projectId;
    private UserBasicDataDto userBasicDataDto;
}
