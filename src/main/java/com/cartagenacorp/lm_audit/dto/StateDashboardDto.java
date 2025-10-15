package com.cartagenacorp.lm_audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateDashboardDto {
    private Long id;
    private String name;
    private String color;
    private Long count;
    private double percentage;
    //private double progress;
}
