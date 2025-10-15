package com.cartagenacorp.lm_audit.mapper;

import com.cartagenacorp.lm_audit.dto.IssueHistoryDtoRequest;
import com.cartagenacorp.lm_audit.dto.IssueHistoryDtoResponse;
import com.cartagenacorp.lm_audit.entity.IssueHistory;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IssueHistoryMapper {
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "beforeChange", source = "beforeChange")
    @Mapping(target = "afterChange", source = "afterChange")
    IssueHistory toEntity(IssueHistoryDtoRequest issueHistoryDTO);

    IssueHistoryDtoRequest toDto(IssueHistory issueHistory);

    @Mapping(target = "beforeChange", expression = "java(convertJsonStringToObject(issueHistory.getBeforeChange()))")
    @Mapping(target = "afterChange", expression = "java(convertJsonStringToObject(issueHistory.getAfterChange()))")
    IssueHistoryDtoResponse toDtoResponse(IssueHistory issueHistory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    IssueHistory partialUpdate(IssueHistoryDtoRequest issueHistoryDTO, @MappingTarget IssueHistory issueHistory);

    default Object convertJsonStringToObject(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return objectMapper.readValue(jsonString, Object.class);
        } catch (Exception e) {
            return null;
        }
    }
}