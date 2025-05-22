package com.arthur.tasktrackerapi.project.service;

import com.arthur.tasktrackerapi.exception.handler.ProjectNotFoundException;
import com.arthur.tasktrackerapi.exception.handler.UserNotFoundException;
import com.arthur.tasktrackerapi.project.dto.ProjectRequestDto;
import com.arthur.tasktrackerapi.project.dto.ProjectResponseDto;
import com.arthur.tasktrackerapi.project.dto.filter.ProjectFilterRequest;
import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.project.mapper.ProjectMapper;
import com.arthur.tasktrackerapi.project.repository.ProjectRepository;
import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<ProjectResponseDto> getAllByOwner(User owner, ProjectFilterRequest filter, Pageable pageable) {
        var projects = projectRepository.getAllByOwnerFiltered(owner, filter, pageable);

        return projects.map(projectMapper::toDto);
    }

    @Override
    public ProjectResponseDto getByIdAndOwner(Long id, User owner) {
        var project = projectRepository.findByIdAndOwnerAndArchivedFalse(id, owner)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        return projectMapper.toDto(project);
    }

    @Override
    public ProjectResponseDto update(Long id, User owner, ProjectRequestDto dto) {
        var project = projectRepository.findByIdAndOwnerAndArchivedFalse(id, owner)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        var updatedProject = projectRepository.save(project);

        return projectMapper.toDto(updatedProject);
    }

    @Override
    public void delete(Long id, User owner) {
        var project = projectRepository.findByIdAndOwnerAndArchivedFalse(id, owner)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        project.setArchived(true);

        projectRepository.save(project);
    }

    @Override
    public ProjectResponseDto unarchive(Long id, User owner) {
        var project = projectRepository.findByIdAndOwnerAndArchivedTrue(id, owner)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        project.setArchived(false);

        var savedProject = projectRepository.save(project);

        return projectMapper.toDto(savedProject);
    }

    @Override
    public List<ProjectResponseDto> getArchivedByOwner(User owner) {
        var archivedProjects = projectRepository.findAllArchivedByOwnerId(owner.getId());
        System.out.println("Archived projects found: " + archivedProjects.size());

        System.out.println("owner.id = " + owner.getId());
        System.out.println("owner.email = " + owner.getEmail());


        return archivedProjects.stream()
                .map(projectMapper::toDto)
                .toList();
    }
}
