package com.arthur.tasktrackerapi.audit.mapper;

import com.arthur.tasktrackerapi.audit.dto.TaskAuditDto;
import com.arthur.tasktrackerapi.audit.entity.TaskAudit;
import org.springframework.stereotype.Service;

@Service
public class TaskAuditMapper {
    public TaskAuditDto toDto(TaskAudit entity) {
        return TaskAuditDto.builder()
                .action(entity.getAction())
                .field(entity.getField())
                .oldValue(entity.getOldValue())
                .newValue(entity.getNewValue())
                .performedBy(entity.getPerformedBy())
                .timestamp(entity.getTimestamp())
                .build();
    }
}
