package com.arthur.tasktrackerapi.user.service;

import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import com.arthur.tasktrackerapi.user.dto.UserResponseDto;
import com.arthur.tasktrackerapi.user.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    UserResponseDto save(UserRequestDto userRequestDto);
    UserResponseDto findById(Long id);
    List<UserResponseDto> findAll();
    void deleteById(Long Id);
    User findByEmail(String email);
}
