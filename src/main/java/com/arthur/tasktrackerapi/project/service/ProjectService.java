package com.arthur.tasktrackerapi.project.service;

import com.arthur.tasktrackerapi.project.dto.ProjectRequestDto;
import com.arthur.tasktrackerapi.project.dto.ProjectResponseDto;
import com.arthur.tasktrackerapi.user.entity.User;

import java.util.List;

public interface ProjectService {

    ProjectResponseDto create(ProjectRequestDto dto, User owner);

    List<ProjectResponseDto> getAllByOwner(User owner);

    ProjectResponseDto getByIdAndOwner(Long id, User owner);

    ProjectResponseDto update(Long id, User owner, ProjectRequestDto dto);

    void delete(Long id, User owner);

}
