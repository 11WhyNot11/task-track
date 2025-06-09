package com.arthur.tasktrackerapi.comment.mapper;

import com.arthur.tasktrackerapi.comment.dto.CommentRequestDto;
import com.arthur.tasktrackerapi.comment.dto.CommentResponseDto;
import com.arthur.tasktrackerapi.comment.entity.Comment;
import com.arthur.tasktrackerapi.task.entity.Task;
import com.arthur.tasktrackerapi.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CommentRequestDto dto, Task task, User author) {
        return Comment.builder()
                .content(dto.getContent())
                .task(task)
                .author(author)
                .build();
    }

    public CommentResponseDto toDto(Comment comment) {
        String fullUrl = null;
        if(comment.getAttachmentPath() != null) {
            fullUrl = "http://localhost:8080" + comment.getAttachmentPath();
        }

        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .taskId(comment.getTask().getId())
                .attachmentUrl(fullUrl)
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName())
                .build();
    }
}
