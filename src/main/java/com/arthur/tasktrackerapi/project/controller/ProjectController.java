package com.arthur.tasktrackerapi.project.controller;

import com.arthur.tasktrackerapi.project.dto.ProjectRequestDto;
import com.arthur.tasktrackerapi.project.dto.ProjectResponseDto;
import com.arthur.tasktrackerapi.project.service.ProjectService;
import com.arthur.tasktrackerapi.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;


    @PostMapping
    public ProjectResponseDto create(@Valid @RequestBody ProjectRequestDto dto, @AuthenticationPrincipal User owner) {
        return projectService.create(dto,owner);
    }

    @GetMapping
    public List<ProjectResponseDto> getAllByOwner(@AuthenticationPrincipal User owner) {
        return projectService.getAllByOwner(owner);
    }

    @GetMapping("/{id}")
    public ProjectResponseDto getByIdAndOwner(@PathVariable Long id,@AuthenticationPrincipal User owner) {
        return projectService.getByIdAndOwner(id, owner);
    }

    @PutMapping("/{id}")
    public ProjectResponseDto update(@PathVariable Long id, @AuthenticationPrincipal User owner,@Valid @RequestBody ProjectRequestDto dto) {
        return projectService.update(id, owner, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User owner) {
        projectService.delete(id, owner);
        return ResponseEntity.noContent().build();
    }

}
