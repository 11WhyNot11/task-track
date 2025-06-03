package com.arthur.tasktrackerapi.audit.service;

import com.arthur.tasktrackerapi.audit.dto.CommentAuditDto;
import com.arthur.tasktrackerapi.audit.entity.AuditAction;
import com.arthur.tasktrackerapi.audit.entity.CommentAudit;
import com.arthur.tasktrackerapi.audit.mapper.CommentAuditMapper;
import com.arthur.tasktrackerapi.audit.repository.CommentAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentAuditServiceImpl implements CommentAuditService {

    private final CommentAuditRepository commentAuditRepository;
    private final CommentAuditMapper commentAuditMapper;

    @Override
    public List<CommentAuditDto> getAuditForComment(Long commentId) {
        var audits = commentAuditRepository.findAllByCommentId(commentId);

        return audits.stream()
                .map(commentAuditMapper::toDto)
                .toList();
    }

    @Override
    public void saveAuditEntry(Long commentId,
                               AuditAction action,
                               String field,
                               String oldValue,
                               String newValue,
                               String performedBy) {
        var audit = CommentAudit.builder()
                .commentId(commentId)
                .action(action)
                .field(field)
                .oldValue(oldValue)
                .newValue(newValue)
                .performedBy(performedBy)
                .timestamp(LocalDateTime.now())
                .build();

        commentAuditRepository.save(audit);
    }
}
