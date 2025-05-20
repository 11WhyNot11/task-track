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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;
    private final AccessValidator accessValidator;

    @Override
    public CommentResponseDto create(CommentRequestDto dto, User author) {
        var task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException(dto.getTaskId()));

        accessValidator.validateAccessToTask(task, author);

        var comment = Comment.builder()
                .content(dto.getContent())
                .task(task)
                .author(author)
                .build();

        var savedComment = commentRepository.save(comment);

        return commentMapper.toDto(savedComment);


    }

    @Override
    public List<CommentResponseDto> getAllByTaskId(Long taskId, User currentUser) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        accessValidator.validateAccessToTask(task, currentUser);

        var comments = commentRepository.findAllByTask_Id(taskId);

        return comments.stream()
                .map(commentMapper::toDto)
                .toList();

    }

    @Override
    public CommentResponseDto update(CommentRequestDto dto, Long commentId, User currentUser) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        accessValidator.validateCanModifyComment(comment, currentUser);

        comment.setContent(dto.getContent());

        var updatedComment = commentRepository.save(comment);

        return commentMapper.toDto(updatedComment);
    }

    @Override
    public void delete(Long commentId, User currentUser) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        accessValidator.validateCanModifyComment(comment, currentUser);

        commentRepository.delete(comment);
    }
}
