package com.arthur.tasktrackerapi.task.service;

import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.task.dto.TaskRequestDto;
import com.arthur.tasktrackerapi.task.dto.TaskResponseDto;
import com.arthur.tasktrackerapi.task.dto.filter.TaskFilterRequest;
import com.arthur.tasktrackerapi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskResponseDto create(TaskRequestDto dto, User currentUser);
    Page<TaskResponseDto> getAllByProjectId(Long projectId, TaskFilterRequest filter, Pageable pageable, User currentUser);
    TaskResponseDto getById(Long id, User currentUser);
    TaskResponseDto update(TaskRequestDto request, Long taskId, User currentUser);
    void delete(Long id, User currentUser);
    TaskResponseDto unarchive(Long id, User currentUser);
    List<TaskResponseDto> getArchivedByProject(Long projectId, User currentUser);
}
