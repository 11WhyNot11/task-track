package com.arthur.tasktrackerapi.audit.mapper;

import com.arthur.tasktrackerapi.audit.dto.CommentAuditDto;
import com.arthur.tasktrackerapi.audit.entity.CommentAudit;
import com.arthur.tasktrackerapi.comment.entity.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentAuditMapper {
    public CommentAuditDto toDto(CommentAudit entity) {
        return CommentAuditDto.builder()
                .action(entity.getAction())
                .field(entity.getField())
                .oldValue(entity.getOldValue())
                .newValue(entity.getNewValue())
                .performedBy(entity.getPerformedBy())
                .timestamp(entity.getTimestamp())
                .build();
    }
}
