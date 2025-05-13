package com.arthur.tasktrackerapi.project.controller;

import com.arthur.tasktrackerapi.project.dto.ProjectRequestDto;
import com.arthur.tasktrackerapi.project.dto.ProjectResponseDto;
import com.arthur.tasktrackerapi.project.service.ProjectService;
import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final UserService userService; // додай цей сервіс через DI

    private User getCurrentUser(UserDetails userDetails) {
        return userService.findByEmail(userDetails.getUsername());
    }

    @PostMapping
    public ProjectResponseDto create(@Valid @RequestBody ProjectRequestDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        User owner = getCurrentUser(userDetails);
        return projectService.create(dto, owner);
    }

    @GetMapping
    public List<ProjectResponseDto> getAllByOwner(@AuthenticationPrincipal UserDetails userDetails) {
        User owner = getCurrentUser(userDetails);
        return projectService.getAllByOwner(owner);
    }

    @GetMapping("/{id}")
    public ProjectResponseDto getByIdAndOwner(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User owner = getCurrentUser(userDetails);
        return projectService.getByIdAndOwner(id, owner);
    }

    @PutMapping("/{id}")
    public ProjectResponseDto update(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ProjectRequestDto dto) {
        User owner = getCurrentUser(userDetails);
        return projectService.update(id, owner, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User owner = getCurrentUser(userDetails);
        projectService.delete(id, owner);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}")
    public ProjectResponseDto unarchive(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User owner = getCurrentUser(userDetails);

        return  projectService.unarchive(id, owner);

    }

    @GetMapping("/archived")
    public List<ProjectResponseDto> getArchivedByOwner(@AuthenticationPrincipal UserDetails userDetails) {
        var email = userDetails.getUsername();
        var user = userService.findByEmail(email);


        return projectService.getArchivedByOwner(user);
    }
}
