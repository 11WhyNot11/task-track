package com.arthur.tasktrackerapi.task.controller;

import com.arthur.tasktrackerapi.task.dto.TaskRequestDto;
import com.arthur.tasktrackerapi.task.dto.TaskResponseDto;
import com.arthur.tasktrackerapi.task.service.TaskService;
import com.arthur.tasktrackerapi.user.entity.User;
import lombok.RequiredArgsConstructor;
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


    @PostMapping
    public ResponseEntity<TaskResponseDto> create(@RequestBody TaskRequestDto dto,@AuthenticationPrincipal User currentUser) {
        var taskDto = taskService.create(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDto);
    }
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDto>> getAllByProjectId(@PathVariable Long projectId,@AuthenticationPrincipal User currentUser) {
        var tasksDto = taskService.getAllByProjectId(projectId, currentUser);
        return ResponseEntity.ok(tasksDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getById(@PathVariable Long id,@AuthenticationPrincipal User currentUser) {
        var taskDto = taskService.getById(id, currentUser);
        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(@RequestBody TaskRequestDto dto,
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
