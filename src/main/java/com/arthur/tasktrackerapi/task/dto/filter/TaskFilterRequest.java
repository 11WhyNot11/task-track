package com.arthur.tasktrackerapi.task.dto.filter;

import com.arthur.tasktrackerapi.task.entity.Priority;
import com.arthur.tasktrackerapi.task.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskFilterRequest {

    private Status status;
    private Priority priority;
    private OffsetDateTime deadline;
}
