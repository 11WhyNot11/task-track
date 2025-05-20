package com.arthur.tasktrackerapi.project.dto;

import com.arthur.tasktrackerapi.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponseDto {

    private Long id;
    private String name;
    private String description;
    private boolean archived;
    private LocalDateTime createdAt;
    private Long ownerId;

}
