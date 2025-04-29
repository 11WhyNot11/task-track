package com.arthur.tasktrackerapi.user.mapper;

import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import com.arthur.tasktrackerapi.user.dto.UserResponseDto;
import com.arthur.tasktrackerapi.user.entity.User;

public class UserMapper {
    public static User toEntity(UserRequestDto userRequestDto) {
        return User.builder()
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .role(userRequestDto.getRole())
                .build();
    }

    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}
