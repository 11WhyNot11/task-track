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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
        var project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(dto.getProjectId()));

        accessValidator.validateAccessToProject(project, currentUser);

        var task = taskMapper.toEntity(dto, project);

        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        task.setArchived(false);

        var saved = taskRepository.save(task);

        return taskMapper.toDto(saved);
    }

    @Override
    public Page<TaskResponseDto> getAllByProjectId(Long projectId, TaskFilterRequest filter, Pageable pageable, User currentUser) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        accessValidator.validateAccessToProject(project, currentUser);

        var page = taskRepository.findAllFiltered(projectId, filter, pageable);
        return page.map(taskMapper::toDto);
    }

    @Override
    public TaskResponseDto getById(Long id, User currentUser) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        accessValidator.validateAccessToProject(task.getProject(), currentUser);

        return taskMapper.toDto(task);
    }

    @Override
    public TaskResponseDto update(TaskRequestDto dto, Long id, User currentUser) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        accessValidator.validateAccessToProject(task.getProject(), currentUser);

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDeadline(dto.getDeadline());

        var saved = taskRepository.save(task);

        return taskMapper.toDto(saved);
    }

    @Override
    public void delete(Long id, User currentUser) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        accessValidator.validateAccessToProject(task.getProject(), currentUser);

        task.setArchived(true);

        taskRepository.save(task);
    }

    @Override
    public TaskResponseDto unarchive(Long id, User currentUser) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        accessValidator.validateAccessToProject(task.getProject(), currentUser);

        task.setArchived(false);

        var saved = taskRepository.save(task);

        return taskMapper.toDto(saved);
    }

    @Override
    public List<TaskResponseDto> getArchivedByProject(Long projectId, User currentUser) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        accessValidator.validateAccessToProject(project, currentUser);

        var archivedTasks = taskRepository.findAllByProject_IdAndArchivedTrue(projectId);

        return archivedTasks.stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
