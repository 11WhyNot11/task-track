package com.arthur.tasktrackerapi.task.repository;

import com.arthur.tasktrackerapi.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {
    List<Task> findAllByProject_IdAndArchivedFalse(Long projectId);
    List<Task> findAllByProject_IdAndArchivedTrue(Long projectId);
    Optional<Task> findByIdAndArchivedFalse(Long id);

}
