package com.cartagenacorp.lm_audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NamedIdDTO {
    private Long id;
    private String name;
    private String color;
    private Integer orderIndex;
    private String organizationId;
}
