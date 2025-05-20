package com.arthur.tasktrackerapi.comment.controller;

import com.arthur.tasktrackerapi.comment.dto.CommentRequestDto;
import com.arthur.tasktrackerapi.comment.dto.CommentResponseDto;
import com.arthur.tasktrackerapi.comment.service.CommentService;
import com.arthur.tasktrackerapi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> create(
            @RequestBody CommentRequestDto dto,
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
            @RequestBody CommentRequestDto dto,
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
}

