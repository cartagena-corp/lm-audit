package com.cartagenacorp.lm_audit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "issue_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "issue_id", nullable = false)
    private UUID issueId;

    @Column(name = "issue_title", nullable = false)
    private String issueTitle;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String beforeChange;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String afterChange;
}
