package com.arthur.tasktrackerapi.task.service;

import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.task.dto.TaskRequestDto;
import com.arthur.tasktrackerapi.task.dto.TaskResponseDto;
import com.arthur.tasktrackerapi.user.entity.User;

import java.util.List;

public interface TaskService {
    TaskResponseDto create(TaskRequestDto dto, User currentUser);
    List<TaskResponseDto> getAllByProjectId(Long projectId, User currentUser);
    TaskResponseDto getById(Long id, User currentUser);
    TaskResponseDto update(TaskRequestDto dto, Long id, User currentUser);
    void delete(Long id, User currentUser);
    TaskResponseDto unarchive(Long id, User currentUser);
    List<TaskResponseDto> getArchivedByProject(Long projectId, User currentUser);
}
