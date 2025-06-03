package com.arthur.tasktrackerapi.task.controller;

import com.arthur.tasktrackerapi.audit.dto.TaskAuditDto;
import com.arthur.tasktrackerapi.audit.service.TaskAuditService;
import com.arthur.tasktrackerapi.exception.handler.TaskNotFoundException;
import com.arthur.tasktrackerapi.exception.handler.UserNotFoundException;
import com.arthur.tasktrackerapi.security.access.AccessValidator;
import com.arthur.tasktrackerapi.task.dto.TaskRequestDto;
import com.arthur.tasktrackerapi.task.dto.TaskResponseDto;
import com.arthur.tasktrackerapi.task.dto.filter.TaskFilterRequest;
import com.arthur.tasktrackerapi.task.entity.Task;
import com.arthur.tasktrackerapi.task.mapper.TaskMapper;
import com.arthur.tasktrackerapi.task.repository.TaskRepository;
import com.arthur.tasktrackerapi.task.service.TaskService;
import com.arthur.tasktrackerapi.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskAuditService taskAuditService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final AccessValidator accessValidator;

    @GetMapping("/{id}/audit")
    public ResponseEntity<List<TaskAuditDto>> getTaskAudit(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        var task = taskRepository.findByIdAndArchivedFalse(id)
                        .orElseThrow(() -> new TaskNotFoundException(id));
        accessValidator.validateAccessToTask(task, currentUser);

        return ResponseEntity.ok(taskAuditService.getAuditForTask(id));
    }


    @PostMapping
    public ResponseEntity<TaskResponseDto> create(@Valid @RequestBody TaskRequestDto dto,
                                                  @AuthenticationPrincipal User currentUser) {
        var taskDto = taskService.create(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDto);
    }
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Page<TaskResponseDto>> getAllByProjectId(@PathVariable Long projectId,
                                                                   @AuthenticationPrincipal User currentUser,
                                                                   TaskFilterRequest filter,
                                                                   Pageable pageable) {
        Page<TaskResponseDto> dtoPage = taskService.getAllByProjectId(projectId, filter, pageable, currentUser);
        return ResponseEntity.ok(dtoPage);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getById(@PathVariable Long id,@AuthenticationPrincipal User currentUser) {
        var taskDto = taskService.getById(id, currentUser);
        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(@Valid @RequestBody TaskRequestDto dto,
                                                  @PathVariable Long id,
                                                  @AuthenticationPrincipal User currentUser) {
        var updatedDto = taskService.update(dto, id, currentUser);
        return ResponseEntity.ok(updatedDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,@AuthenticationPrincipal User currentUser) {
        taskService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDto> unarchive(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        var unarchivedDto = taskService.unarchive(id, currentUser);
        return ResponseEntity.ok(unarchivedDto);
    }

    @GetMapping("/project/{projectId}/archived")
    public ResponseEntity<List<TaskResponseDto>> getArchivedByProject(@PathVariable Long projectId,@AuthenticationPrincipal User currentUser) {
        var archivedByProject = taskService.getArchivedByProject(projectId, currentUser);
        return ResponseEntity.ok(archivedByProject);
    }

}
