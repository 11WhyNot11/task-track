package com.arthur.tasktrackerapi.user.service;

import com.arthur.tasktrackerapi.exception.handler.UserNotFoundException;
import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import com.arthur.tasktrackerapi.user.dto.UserResponseDto;
import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.dto.filter.UserFilterRequest;
import com.arthur.tasktrackerapi.user.mapper.UserMapper;
import com.arthur.tasktrackerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        log.info("Creating user '{}'", userRequestDto.getEmail());
        var user = UserMapper.toEntity(userRequestDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var savedUser = userRepository.save(user);
        log.debug("Created user '{}'", userRequestDto.getEmail());

        return UserMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto findById(Long id) {
        log.info("Fetching user by ID '{}'", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with ID '{}' not found", id);
                    return new UserNotFoundException(id);
                });

        return UserMapper.toDto(user);
    }

    @Override
    public Page<UserResponseDto> findAll(UserFilterRequest filter, Pageable pageable) {
        log.info("Fetching all users");
        var filtered = userRepository.findAllFiltered(filter, pageable);

        log.debug("Found '{}' users", filtered.getTotalElements());

        return filtered.map(UserMapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting user by ID '{}'", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }

    @Override
    public User findByEmail(String email) {
        log.info("Fetching user by email: '{}'", email);
        var foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + email));

        log.debug("Fetched user '{}'", foundUser.getEmail());

        return foundUser;
    }
}
