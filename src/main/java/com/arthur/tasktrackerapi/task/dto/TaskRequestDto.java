package com.arthur.tasktrackerapi.task.dto;

import com.arthur.tasktrackerapi.task.entity.Priority;
import com.arthur.tasktrackerapi.task.entity.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Status status;

    private Priority priority;

    @FutureOrPresent(message = "Deadline must be in the future or now")
    private LocalDateTime deadline;

    @NotNull(message = "Project ID is required")
    private Long projectId;

}
