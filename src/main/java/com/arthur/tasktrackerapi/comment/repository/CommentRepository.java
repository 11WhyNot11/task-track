package com.arthur.tasktrackerapi.comment.repository;

import com.arthur.tasktrackerapi.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTask_Id(Long TaskId);
    Optional<Comment> findByIdAndAuthor_Id(Long id, Long authorId);
}

