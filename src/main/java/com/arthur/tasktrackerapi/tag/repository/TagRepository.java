package com.arthur.tasktrackerapi.tag.repository;

import com.arthur.tasktrackerapi.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
