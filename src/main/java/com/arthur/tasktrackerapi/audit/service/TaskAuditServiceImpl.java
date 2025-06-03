package com.arthur.tasktrackerapi.audit.service;

import com.arthur.tasktrackerapi.audit.dto.TaskAuditDto;
import com.arthur.tasktrackerapi.audit.entity.AuditAction;
import com.arthur.tasktrackerapi.audit.entity.TaskAudit;
import com.arthur.tasktrackerapi.audit.mapper.TaskAuditMapper;
import com.arthur.tasktrackerapi.audit.repository.TaskAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskAuditServiceImpl implements TaskAuditService {

    private final TaskAuditRepository taskAuditRepository;
    private final TaskAuditMapper taskAuditMapper;

    @Override
    public List<TaskAuditDto> getAuditForTask(Long taskId) {
        var audits = taskAuditRepository.findByTaskId(taskId);

        return audits.stream()
                .map(taskAuditMapper::toDto)
                .toList();
    }

    @Override
    public void saveAuditEntry(Long taskId, AuditAction action, String field, String oldValue, String newValue, String performedBy) {
        var audit = TaskAudit.builder()
                .taskId(taskId)
                .action(action)
                .field(field)
                .oldValue(oldValue)
                .newValue(newValue)
                .performedBy(performedBy)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("AUDIT: [{}] {}: '{}' -> '{}'", action, field, oldValue, newValue);


        taskAuditRepository.save(audit);
    }
}
