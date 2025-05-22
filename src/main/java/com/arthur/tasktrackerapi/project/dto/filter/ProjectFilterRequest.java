package com.arthur.tasktrackerapi.project.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectFilterRequest {
    private String name;
    private Long ownerId;
}
