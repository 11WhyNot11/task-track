package com.arthur.tasktrackerapi.project.mapper;

import com.arthur.tasktrackerapi.project.dto.ProjectRequestDto;
import com.arthur.tasktrackerapi.project.dto.ProjectResponseDto;
import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectRequestDto dto, User owner) {
        return Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .owner(owner)
                .build();
    }

    public ProjectResponseDto toDto(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .archived(project.isArchived())
                .createdAt(project.getCreatedAt())
                .ownerId(project.getOwner().getId())
                .build();
    }
}
