package com.arthur.tasktrackerapi.audit.dto;

import com.arthur.tasktrackerapi.audit.entity.AuditAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentAuditDto {
   private AuditAction action;

    private String field;

    private String oldValue;

    private String newValue;

    private String performedBy;

    private LocalDateTime timestamp;
}
