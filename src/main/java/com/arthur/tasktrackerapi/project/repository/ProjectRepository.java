package com.arthur.tasktrackerapi.project.repository;

import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {

    List<Project> findAllByOwnerAndArchivedFalse(User owner);
    List<Project> findAllByOwnerAndArchivedTrue(User owner);

    @Query("SELECT p FROM Project p WHERE p.owner.id = :ownerId AND p.archived = true")
    List<Project> findAllArchivedByOwnerId(@Param("ownerId") Long ownerId);


    Optional<Project> findByIdAndOwnerAndArchivedTrue(Long id, User owner);
    Optional<Project> findByIdAndOwnerAndArchivedFalse(Long id, User owner);
}
