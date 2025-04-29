package com.arthur.tasktrackerapi.user.repository;

import com.arthur.tasktrackerapi.user.entity.User;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Long id(Long id);
}
