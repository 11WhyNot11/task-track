package com.arthur.tasktrackerapi.audit.repository;

import com.arthur.tasktrackerapi.audit.entity.CommentAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentAuditRepository extends JpaRepository<CommentAudit, Long> {
    List<CommentAudit> findAllByCommentId(Long commentId);
}
