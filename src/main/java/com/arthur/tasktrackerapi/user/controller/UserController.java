package com.arthur.tasktrackerapi.user.controller;

import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import com.arthur.tasktrackerapi.user.dto.UserResponseDto;
import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.mapper.UserMapper;
import com.arthur.tasktrackerapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> save(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto createdUser = userService.save(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        UserResponseDto user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<UserResponseDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public UserResponseDto me(Authentication authentication) {
        var email = authentication.getName();
        var user = userService.findByEmail(email);

        return UserMapper.toDto(user);
    }
}
