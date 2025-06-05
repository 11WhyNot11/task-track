package com.arthur.tasktrackerapi.tag.service;

import com.arthur.tasktrackerapi.tag.dto.TagRequestDto;
import com.arthur.tasktrackerapi.tag.dto.TagResponseDto;
import com.arthur.tasktrackerapi.user.entity.User;

import java.util.List;

public interface TagService {
        TagResponseDto create(TagRequestDto dto);
        List<TagResponseDto> findAllTags();
}
