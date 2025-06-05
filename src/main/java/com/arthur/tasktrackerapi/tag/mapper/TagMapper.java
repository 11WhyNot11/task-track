package com.arthur.tasktrackerapi.tag.mapper;

import com.arthur.tasktrackerapi.tag.dto.TagRequestDto;
import com.arthur.tasktrackerapi.tag.dto.TagResponseDto;
import com.arthur.tasktrackerapi.tag.entity.Tag;
import org.springframework.stereotype.Service;

@Service
public class TagMapper {
    public Tag toEntity(TagRequestDto dto) {
        return Tag.builder()
                .name(dto.getName())
                .build();
    }

    public TagResponseDto toDto(Tag tag) {
        return TagResponseDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
}
