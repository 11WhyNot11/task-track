package com.arthur.tasktrackerapi.task.service;

import com.arthur.tasktrackerapi.exception.handler.ProjectNotFoundException;
import com.arthur.tasktrackerapi.exception.handler.TaskNotFoundException;
import com.arthur.tasktrackerapi.exception.handler.UserNotFoundException;
import com.arthur.tasktrackerapi.project.dto.ProjectResponseDto;
import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.project.repository.ProjectRepository;
import com.arthur.tasktrackerapi.security.access.AccessValidator;
import com.arthur.tasktrackerapi.task.dto.TaskRequestDto;
import com.arthur.tasktrackerapi.task.dto.TaskResponseDto;
import com.arthur.tasktrackerapi.task.dto.filter.TaskFilterRequest;
import com.arthur.tasktrackerapi.task.entity.Status;
import com.arthur.tasktrackerapi.task.entity.Task;
import com.arthur.tasktrackerapi.task.mapper.TaskMapper;
import com.arthur.tasktrackerapi.task.repository.TaskRepository;
import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final AccessValidator accessValidator;

    @Override
    public TaskResponseDto create(TaskRequestDto dto, User currentUser) {
        log.info("Creating task in projectId={} by userId={}", dto.getProjectId(), currentUser.getId());

        var project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> {
                    log.warn("Project with id={} not found", dto.getProjectId());
                    return new ProjectNotFoundException(dto.getProjectId());
                });

        accessValidator.validateAccessToProject(project, currentUser);

        var task = taskMapper.toEntity(dto, project);
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        task.setArchived(false);

        var saved = taskRepository.save(task);

        log.debug("Created task with id={} in projectId={}", saved.getId(), project.getId());

        return taskMapper.toDto(saved);
    }

    @Override
    public Page<TaskResponseDto> getAllByProjectId(Long projectId, TaskFilterRequest filter, Pageable pageable, User currentUser) {
        log.info("Fetching all tasks for projectId={} by userId={}", projectId, currentUser.getId());

        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.warn("Project with id={} not found", projectId);
                    return new ProjectNotFoundException(projectId);
                });

        accessValidator.validateAccessToProject(project, currentUser);

        var page = taskRepository.findAllFiltered(projectId, filter, pageable);

        log.debug("Fetched {} tasks for projectId={}", page.getTotalElements(), projectId);

        return page.map(taskMapper::toDto);
    }

    @Override
    public TaskResponseDto getById(Long id, User currentUser) {
        log.info("Fetching task with id={} by userId={}", id, currentUser.getId());

        var task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task with id={} not found", id);
                    return new TaskNotFoundException(id);
                });

        accessValidator.validateAccessToProject(task.getProject(), currentUser);

        return taskMapper.toDto(task);
    }

    @Override
    public TaskResponseDto update(TaskRequestDto dto, Long id, User currentUser) {
        log.info("Updating task id={} by userId={}", id, currentUser.getId());

        var task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task with id={} not found", id);
                    return new TaskNotFoundException(id);
                });

        accessValidator.validateAccessToProject(task.getProject(), currentUser);

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDeadline(dto.getDeadline());

        var saved = taskRepository.save(task);

        log.debug("Updated task id={} by userId={}", saved.getId(), currentUser.getId());

        return taskMapper.toDto(saved);
    }

    @Override
    public void delete(Long id, User currentUser) {
        log.info("Archiving task id={} by userId={}", id, currentUser.getId());

        var task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task with id={} not found", id);
                    return new TaskNotFoundException(id);
                });

        accessValidator.validateAccessToProject(task.getProject(), currentUser);

        task.setArchived(true);
        taskRepository.save(task);

        log.debug("Archived task id={}", id);
    }

    @Override
    public TaskResponseDto unarchive(Long id, User currentUser) {
        log.info("Unarchiving task id={} by userId={}", id, currentUser.getId());

        var task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task with id={} not found", id);
                    return new TaskNotFoundException(id);
                });

        accessValidator.validateAccessToProject(task.getProject(), currentUser);

        task.setArchived(false);

        var saved = taskRepository.save(task);

        log.debug("Unarchived task id={}", saved.getId());

        return taskMapper.toDto(saved);
    }

    @Override
    public List<TaskResponseDto> getArchivedByProject(Long projectId, User currentUser) {
        log.info("Fetching archived tasks for projectId={} by userId={}", projectId, currentUser.getId());

        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.warn("Project with id={} not found", projectId);
                    return new ProjectNotFoundException(projectId);
                });

        accessValidator.validateAccessToProject(project, currentUser);

        var archivedTasks = taskRepository.findAllByProject_IdAndArchivedTrue(projectId);

        log.debug("Found {} archived tasks for projectId={}", archivedTasks.size(), projectId);

        return archivedTasks.stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
