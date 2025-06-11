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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;


    @Override
    public ProjectResponseDto create(ProjectRequestDto dto, User owner) {
        log.info("Creating project '{}' for user '{}'", dto.getName(), owner.getEmail());

        var project = projectMapper.toEntity(dto, owner);
        var savedProject = projectRepository.save(project);

        log.debug("Project created with ID: {}", savedProject.getId());

        return projectMapper.toDto(savedProject);
    }

    @Override
    public Page<ProjectResponseDto> getAllByOwner(User owner, ProjectFilterRequest filter, Pageable pageable) {
        log.info("Fetching projects for user '{}'", owner.getEmail());
        var projects = projectRepository.getAllByOwnerFiltered(owner, filter, pageable);

        log.debug("Fetched '{}' projects for user '{}'", projects.getTotalElements(), owner.getEmail());
        return projects.map(projectMapper::toDto);
    }

    @Override
    public ProjectResponseDto getByIdAndOwner(Long id, User owner) {
        log.info("Fetching project with ID '{}' for user '{}'", id, owner.getEmail());
        var project = projectRepository.findByIdAndOwnerAndArchivedFalse(id, owner)
                .orElseThrow(() -> {
                    log.warn("Project with ID '{}' not found for user '{}'", id, owner.getEmail());
                    return new ProjectNotFoundException(id);
                });

        return projectMapper.toDto(project);
    }

    @Override
    public ProjectResponseDto update(Long id, User owner, ProjectRequestDto dto) {
        log.info("Updating project with ID '{}' for user '{}'", id, owner.getEmail());
        var project = projectRepository.findByIdAndOwnerAndArchivedFalse(id, owner)
                .orElseThrow(() -> {

                    log.warn("Project with ID '{}' not found for update", id);
                    return new ProjectNotFoundException(id);
                });

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        var updatedProject = projectRepository.save(project);

        log.debug("Updated project with ID '{}'", updatedProject.getId());

        return projectMapper.toDto(updatedProject);
    }

    @Override
    public void delete(Long id, User owner) {
        log.info("Archived project with ID '{}' for user '{}'", id, owner.getEmail());
        var project = projectRepository.findByIdAndOwnerAndArchivedFalse(id, owner)
                .orElseThrow(() -> {

                    log.warn("Project with ID '{}' not found for archive", id);
                    return new ProjectNotFoundException(id);
                });

        project.setArchived(true);

        projectRepository.save(project);

        log.debug("Project with ID {} archived", id);
    }

    @Override
    public ProjectResponseDto unarchive(Long id, User owner) {
        log.info("Unarchiving project with ID {} for user '{}'", id, owner.getEmail());

        var project = projectRepository.findByIdAndOwnerAndArchivedTrue(id, owner)
                .orElseThrow(() -> {
                    log.warn("Project with ID {} not found for unarchive", id);
                    return new ProjectNotFoundException(id);
                });

        project.setArchived(false);
        var savedProject = projectRepository.save(project);

        log.debug("Project with ID {} unarchived", savedProject.getId());

        return projectMapper.toDto(savedProject);
    }

    @Override
    public List<ProjectResponseDto> getArchivedByOwner(User owner) {
        log.info("Fetching archived projects for user '{}'", owner.getEmail());

        var archivedProjects = projectRepository.findAllArchivedByOwnerId(owner.getId());

        log.debug("Found {} archived projects for user '{}'", archivedProjects.size(), owner.getEmail());

        return archivedProjects.stream()
                .map(projectMapper::toDto)
                .toList();
    }
}
