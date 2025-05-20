package com.arthur.tasktrackerapi.task.dto;

import com.arthur.tasktrackerapi.task.entity.Priority;
import com.arthur.tasktrackerapi.task.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseDto {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDateTime deadline;
    private Boolean archived;
    private Long projectId;
}
