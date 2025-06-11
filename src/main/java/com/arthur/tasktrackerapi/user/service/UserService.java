package com.arthur.tasktrackerapi.user.service;

import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import com.arthur.tasktrackerapi.user.dto.UserResponseDto;
import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.dto.filter.UserFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDto save(UserRequestDto userRequestDto);
    UserResponseDto findById(Long id);
    Page<UserResponseDto> findAll(UserFilterRequest filter, Pageable pageable);
    void deleteById(Long Id);
    User findByEmail(String email);
}
