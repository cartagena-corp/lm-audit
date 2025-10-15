package com.cartagenacorp.lm_audit.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProjectConfigDTO {
    private Long id;
    private UUID projectId;
    private List<NamedIdDTO> issueStatuses;
    private List<NamedIdDTO> issuePriorities;
    private List<NamedIdDTO> issueTypes;
    private List<NamedIdDTO> sprintStatuses;
    private List<NamedIdDTO> issueDescriptions;
    private UUID organizationId;
}
