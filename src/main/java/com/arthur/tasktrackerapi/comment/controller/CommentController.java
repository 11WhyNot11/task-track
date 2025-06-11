package com.arthur.tasktrackerapi.comment.controller;

import com.arthur.tasktrackerapi.audit.dto.CommentAuditDto;
import com.arthur.tasktrackerapi.audit.service.CommentAuditService;
import com.arthur.tasktrackerapi.comment.dto.CommentRequestDto;
import com.arthur.tasktrackerapi.comment.dto.CommentResponseDto;
import com.arthur.tasktrackerapi.comment.entity.Comment;
import com.arthur.tasktrackerapi.comment.repository.CommentRepository;
import com.arthur.tasktrackerapi.comment.service.CommentService;
import com.arthur.tasktrackerapi.exception.handler.CommentNotFoundException;
import com.arthur.tasktrackerapi.security.access.AccessValidator;
import com.arthur.tasktrackerapi.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentAuditService commentAuditService;
    private final CommentRepository commentRepository;
    private final AccessValidator accessValidator;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentResponseDto> create(
            @Valid @ModelAttribute CommentRequestDto dto,
            @AuthenticationPrincipal User author) {
        var comment = commentService.create(dto, author);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentResponseDto>> getAllByTaskId(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser) {
        var comments = commentService.getAllByTaskId(taskId, currentUser);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> update(
            @PathVariable("id") Long commentId,
            @Valid @RequestBody CommentRequestDto dto,
            @AuthenticationPrincipal User currentUser) {
        var updated = commentService.update(dto, commentId, currentUser);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long commentId,
            @AuthenticationPrincipal User currentUser) {
        commentService.delete(commentId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/audit")
    public ResponseEntity<List<CommentAuditDto>> getCommentAudit(@PathVariable Long id,
                                                                 @AuthenticationPrincipal User currentUser) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        accessValidator.validateCanModifyComment(comment, currentUser);

        return ResponseEntity.ok(commentAuditService.getAuditForComment(id));
    }
}

