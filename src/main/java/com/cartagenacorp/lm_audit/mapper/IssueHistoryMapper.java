package com.cartagenacorp.lm_audit.mapper;

import com.cartagenacorp.lm_audit.dto.IssueHistoryDTO;
import com.cartagenacorp.lm_audit.entity.IssueHistory;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IssueHistoryMapper {
    IssueHistory toEntity(IssueHistoryDTO issueHistoryDTO);

    IssueHistoryDTO toDto(IssueHistory issueHistory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    IssueHistory partialUpdate(IssueHistoryDTO issueHistoryDTO, @MappingTarget IssueHistory issueHistory);
}