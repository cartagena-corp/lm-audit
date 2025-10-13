package com.cartagenacorp.lm_audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueHistoryDtoResponse {

    private UUID id;
    private UUID issueId;
    private String issueTitle;
    private UUID userId;
    private String action;
    private String description;
    private LocalDateTime timestamp;
    private UUID projectId;

    private Object beforeChange;
    private Object afterChange;

    private UserBasicDataDto userBasicDataDto;
}
