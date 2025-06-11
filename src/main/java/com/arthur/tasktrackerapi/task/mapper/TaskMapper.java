package com.arthur.tasktrackerapi.task.mapper;

import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.tag.dto.TagResponseDto;
import com.arthur.tasktrackerapi.tag.entity.Tag;
import com.arthur.tasktrackerapi.tag.mapper.TagMapper;
import com.arthur.tasktrackerapi.task.dto.TaskRequestDto;
import com.arthur.tasktrackerapi.task.dto.TaskResponseDto;
import com.arthur.tasktrackerapi.task.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final TagMapper tagMapper;

    public Task toEntity(TaskRequestDto dto, Project project) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .deadline(dto.getDeadline())
                .archived(false)
                .project(project)
                .build();
    }

    public TaskResponseDto toDto(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .deadline(task.getDeadline())
                .archived(task.getArchived())
                .projectId(task.getProject().getId())
                .tags(convertTags(task.getTags()))
                .build();
    }

    private Set<TagResponseDto> convertTags(Set<Tag> tags) {
        if (tags == null) {
            return Collections.emptySet();
        }

        return tags.stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toSet());
    }
}
