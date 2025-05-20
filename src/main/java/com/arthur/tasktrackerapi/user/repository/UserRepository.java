package com.arthur.tasktrackerapi.user.repository;

import com.arthur.tasktrackerapi.user.entity.User;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Long id(Long id);

    Optional<User> findByEmail(@NotBlank @Email String email);
}
