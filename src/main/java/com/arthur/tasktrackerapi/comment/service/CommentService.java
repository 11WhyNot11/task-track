package com.arthur.tasktrackerapi.comment.service;

import com.arthur.tasktrackerapi.comment.dto.CommentRequestDto;
import com.arthur.tasktrackerapi.comment.dto.CommentResponseDto;
import com.arthur.tasktrackerapi.user.entity.User;

import java.util.List;

public interface CommentService {

    CommentResponseDto create(CommentRequestDto dto, User author);
    List<CommentResponseDto> getAllByTaskId(Long taskId, User currentUser);
    CommentResponseDto update(CommentRequestDto dto, Long commentId, User currentUser);
    void delete(Long commentId, User currentUser);
}
