package com.arthur.tasktrackerapi.user.service;

import com.arthur.tasktrackerapi.exception.handler.UserNotFoundException;
import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import com.arthur.tasktrackerapi.user.dto.UserResponseDto;
import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.dto.filter.UserFilterRequest;
import com.arthur.tasktrackerapi.user.mapper.UserMapper;
import com.arthur.tasktrackerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        var user = UserMapper.toEntity(userRequestDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

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
    public Page<UserResponseDto> findAll(UserFilterRequest filter, Pageable pageable) {
        var filtered = userRepository.findAllFiltered(filter, pageable);

        return filtered.map(UserMapper::toDto);
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
