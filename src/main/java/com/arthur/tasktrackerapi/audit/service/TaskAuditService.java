package com.arthur.tasktrackerapi.audit.service;

import com.arthur.tasktrackerapi.audit.dto.TaskAuditDto;
import com.arthur.tasktrackerapi.audit.entity.AuditAction;

import java.util.List;

public interface TaskAuditService {
    List<TaskAuditDto> getAuditForTask(Long taskId);
    void saveAuditEntry (
            Long taskId,
            AuditAction action,
            String field,
            String oldValue,
            String newValue,
            String performedBy
    );
}
