package com.arthur.tasktrackerapi.project.repository;

import com.arthur.tasktrackerapi.project.dto.filter.ProjectFilterRequest;
import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepositoryCustom {

    Page<Project> getAllByOwnerFiltered(User owner, ProjectFilterRequest filter, Pageable pageable);
}
