package com.arthur.tasktrackerapi.audit.repository;

import com.arthur.tasktrackerapi.audit.entity.TaskAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TaskAuditRepository extends JpaRepository<TaskAudit, Long> {
    List<TaskAudit> findByTaskId(Long taskId);
}
