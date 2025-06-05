package com.arthur.tasktrackerapi.tag.service;

import com.arthur.tasktrackerapi.tag.dto.TagRequestDto;
import com.arthur.tasktrackerapi.tag.dto.TagResponseDto;
import com.arthur.tasktrackerapi.tag.entity.Tag;
import com.arthur.tasktrackerapi.tag.mapper.TagMapper;
import com.arthur.tasktrackerapi.tag.repository.TagRepository;
import com.arthur.tasktrackerapi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagResponseDto create(TagRequestDto dto) {
        var tag = tagMapper.toEntity(dto);

        var savedTag = tagRepository.save(tag);

        return tagMapper.toDto(savedTag);
    }

    @Override
    public List<TagResponseDto> findAllTags() {
        var tags = tagRepository.findAll();
        return tags.stream()
                .map(tagMapper::toDto)
                .toList();
    }
}
