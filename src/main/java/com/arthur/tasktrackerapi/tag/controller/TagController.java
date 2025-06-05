package com.arthur.tasktrackerapi.tag.controller;

import com.arthur.tasktrackerapi.tag.dto.TagRequestDto;
import com.arthur.tasktrackerapi.tag.dto.TagResponseDto;
import com.arthur.tasktrackerapi.tag.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponseDto> create(@Valid @RequestBody TagRequestDto dto) {
        var tagResponseDto = tagService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(tagResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<TagResponseDto>> findAllTags() {
        var tagsResponseDto = tagService.findAllTags();

        return ResponseEntity.ok(tagsResponseDto);
    }
}
