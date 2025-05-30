package com.arthur.tasktrackerapi.comment.service;

import com.arthur.tasktrackerapi.comment.dto.CommentRequestDto;
import com.arthur.tasktrackerapi.comment.dto.CommentResponseDto;
import com.arthur.tasktrackerapi.comment.entity.Comment;
import com.arthur.tasktrackerapi.comment.mapper.CommentMapper;
import com.arthur.tasktrackerapi.comment.repository.CommentRepository;
import com.arthur.tasktrackerapi.exception.handler.CommentNotFoundException;
import com.arthur.tasktrackerapi.exception.handler.TaskNotFoundException;
import com.arthur.tasktrackerapi.security.access.AccessValidator;
import com.arthur.tasktrackerapi.task.entity.Task;
import com.arthur.tasktrackerapi.task.repository.TaskRepository;
import com.arthur.tasktrackerapi.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;
    private final AccessValidator accessValidator;

    @Override
    public CommentResponseDto create(CommentRequestDto dto, User author) {
        log.info("Creating comment on taskId={} by userId={}", dto.getTaskId(), author.getId());

        var task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> {
                    log.warn("Task with id={} not found", dto.getTaskId());
                    return new TaskNotFoundException(dto.getTaskId());
                });

        accessValidator.validateAccessToTask(task, author);

        var comment = Comment.builder()
                .content(dto.getContent())
                .task(task)
                .author(author)
                .build();

        var savedComment = commentRepository.save(comment);

        log.debug("Created comment with id={} on taskId={}", savedComment.getId(), task.getId());
        return commentMapper.toDto(savedComment);
    }

    @Override
    public List<CommentResponseDto> getAllByTaskId(Long taskId, User currentUser) {
        log.info("Fetching all comments for taskId={} by userId={}", taskId, currentUser.getId());

        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task with id={} not found", taskId);
                    return new TaskNotFoundException(taskId);
                });

        accessValidator.validateAccessToTask(task, currentUser);

        var comments = commentRepository.findAllByTask_Id(taskId);

        log.debug("Found {} comments for taskId={}", comments.size(), taskId);
        return comments.stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public CommentResponseDto update(CommentRequestDto dto, Long commentId, User currentUser) {
        log.info("Updating commentId={} by userId={}", commentId, currentUser.getId());

        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.warn("Comment with id={} not found", commentId);
                    return new CommentNotFoundException(commentId);
                });

        accessValidator.validateCanModifyComment(comment, currentUser);

        comment.setContent(dto.getContent());

        var updatedComment = commentRepository.save(comment);

        log.debug("Updated commentId={}", updatedComment.getId());
        return commentMapper.toDto(updatedComment);
    }

    @Override
    public void delete(Long commentId, User currentUser) {
        log.info("Deleting commentId={} by userId={}", commentId, currentUser.getId());

        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.warn("Comment with id={} not found", commentId);
                    return new CommentNotFoundException(commentId);
                });

        accessValidator.validateCanModifyComment(comment, currentUser);

        commentRepository.delete(comment);

        log.debug("Deleted commentId={}", commentId);
    }
}
