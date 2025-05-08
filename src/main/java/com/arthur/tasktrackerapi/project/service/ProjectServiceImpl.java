package com.arthur.tasktrackerapi.project.service;

import com.arthur.tasktrackerapi.exception.handler.ProjectNotFoundException;
import com.arthur.tasktrackerapi.exception.handler.UserNotFoundException;
import com.arthur.tasktrackerapi.project.dto.ProjectRequestDto;
import com.arthur.tasktrackerapi.project.dto.ProjectResponseDto;
import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.project.mapper.ProjectMapper;
import com.arthur.tasktrackerapi.project.repository.ProjectRepository;
import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectResponseDto create(ProjectRequestDto dto, User owner) {
        var project = projectMapper.toEntity(dto, owner);
        var savedProject = projectRepository.save(project);

        return projectMapper.toDto(savedProject);
    }

    @Override
    public List<ProjectResponseDto> getAllByOwner(User owner) {
        var project = projectRepository.findAllByOwner(owner);

        return project.stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    public ProjectResponseDto getByIdAndOwner(Long id, User owner) {
        var project = projectRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        return projectMapper.toDto(project);
    }

    @Override
    public ProjectResponseDto update(Long id, User owner, ProjectRequestDto dto) {
        var project = projectRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        var updatedProject = projectRepository.save(project);

        return projectMapper.toDto(updatedProject);
    }

    @Override
    public void delete(Long id, User owner) {
        var project = projectRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        project.setArchived(true);

        projectRepository.save(project);
    }
}
