package com.arthur.tasktrackerapi.project.service;

import com.arthur.tasktrackerapi.project.dto.ProjectRequestDto;
import com.arthur.tasktrackerapi.project.dto.ProjectResponseDto;
import com.arthur.tasktrackerapi.project.dto.filter.ProjectFilterRequest;
import com.arthur.tasktrackerapi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    ProjectResponseDto create(ProjectRequestDto dto, User owner);

    Page<ProjectResponseDto> getAllByOwner(User owner, ProjectFilterRequest filter, Pageable pageable);

    ProjectResponseDto getByIdAndOwner(Long id, User owner);

    ProjectResponseDto update(Long id, User owner, ProjectRequestDto dto);

    void delete(Long id, User owner);

    ProjectResponseDto unarchive(Long id, User owner);

    List<ProjectResponseDto> getArchivedByOwner(User owner);

}
