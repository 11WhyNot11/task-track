package com.arthur.tasktrackerapi.audit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;

    @Enumerated(value = EnumType.STRING)
    private AuditAction action;

    private String field;

    private String oldValue;

    private String newValue;

    private String performedBy;

    private LocalDateTime timestamp;
}
