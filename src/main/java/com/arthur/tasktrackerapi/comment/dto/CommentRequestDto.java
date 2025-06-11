package com.arthur.tasktrackerapi.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {

    @NotBlank(message = "Content must not be blank")
    private String content;

    @NotNull(message = "Task ID is required")
    private Long taskId;

    private MultipartFile attachment;
}
