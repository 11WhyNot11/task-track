package com.arthur.tasktrackerapi.audit.service;

import com.arthur.tasktrackerapi.audit.dto.CommentAuditDto;
import com.arthur.tasktrackerapi.audit.entity.AuditAction;

import java.util.List;

public interface CommentAuditService {
    List<CommentAuditDto> getAuditForComment(Long commentId);
    void saveAuditEntry (
            Long commentId,
            AuditAction action,
            String field,
            String oldValue,
            String newValue,
            String performedBy
    );
}
