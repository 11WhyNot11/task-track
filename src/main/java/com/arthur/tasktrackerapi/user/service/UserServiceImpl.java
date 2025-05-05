package com.arthur.tasktrackerapi.user.service;

import com.arthur.tasktrackerapi.exception.handler.UserNotFoundException;
import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import com.arthur.tasktrackerapi.user.dto.UserResponseDto;
import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.mapper.UserMapper;
import com.arthur.tasktrackerapi.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        var user = UserMapper.toEntity(userRequestDto);
        var savedUser = userRepository.save(user);

        return UserMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto findById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return UserMapper.toDto(user);
    }

    @Override
    public List<UserResponseDto> findAll() {
        var users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + email));
    }
}
