package com.arthur.tasktrackerapi.project.controller;

import com.arthur.tasktrackerapi.project.dto.ProjectRequestDto;
import com.arthur.tasktrackerapi.project.dto.ProjectResponseDto;
import com.arthur.tasktrackerapi.project.dto.filter.ProjectFilterRequest;
import com.arthur.tasktrackerapi.project.service.ProjectService;
import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ProjectResponseDto create(@Valid @RequestBody ProjectRequestDto dto,
                                     @AuthenticationPrincipal User user) {
        return projectService.create(dto, user);
    }

    @GetMapping
    public Page<ProjectResponseDto> getAllByOwner(@AuthenticationPrincipal User user,
                                                  @ModelAttribute ProjectFilterRequest filter,
                                                  Pageable pageable) {
        return projectService.getAllByOwner(user, filter, pageable);
    }

    @GetMapping("/{id}")
    public ProjectResponseDto getByIdAndOwner(@PathVariable Long id,
                                              @AuthenticationPrincipal User user) {
        return projectService.getByIdAndOwner(id, user);
    }

    @PutMapping("/{id}")
    public ProjectResponseDto update(@PathVariable Long id,
                                     @AuthenticationPrincipal User user,
                                     @Valid @RequestBody ProjectRequestDto dto) {
        return projectService.update(id, user, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal User user) {
        projectService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ProjectResponseDto unarchive(@PathVariable Long id,
                                        @AuthenticationPrincipal User user) {
        return projectService.unarchive(id, user);
    }

    @GetMapping("/archived")
    public List<ProjectResponseDto> getArchivedByOwner(@AuthenticationPrincipal User user) {
        return projectService.getArchivedByOwner(user);
    }
}
