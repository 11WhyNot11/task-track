package com.arthur.tasktrackerapi.project.repository;

import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByOwner(User owner);

    Optional<Project> findByIdAndOwner(Long id, User owner);
}
