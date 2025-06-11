package com.arthur.tasktrackerapi.task.repository;

import com.arthur.tasktrackerapi.task.dto.TaskResponseDto;
import com.arthur.tasktrackerapi.task.dto.filter.TaskFilterRequest;
import com.arthur.tasktrackerapi.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskRepositoryCustom {
    Page<Task> findAllFiltered(Long projectId, TaskFilterRequest filter, Pageable pageable);
}
