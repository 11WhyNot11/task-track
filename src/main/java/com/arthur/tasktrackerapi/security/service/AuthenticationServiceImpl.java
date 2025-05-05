package com.arthur.tasktrackerapi.security.service;

import com.arthur.tasktrackerapi.exception.handler.InvalidCredentialsException;
import com.arthur.tasktrackerapi.exception.handler.UserAlreadyExistsException;
import com.arthur.tasktrackerapi.exception.handler.UserNotFoundException;
import com.arthur.tasktrackerapi.security.dto.AuthenticationRequestDto;
import com.arthur.tasktrackerapi.security.dto.AuthenticationResponseDto;
import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import com.arthur.tasktrackerapi.user.entity.Role;
import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.mapper.UserMapper;
import com.arthur.tasktrackerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDto register(UserRequestDto userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(userRequestDto.getEmail());
        }

        var user = User.builder()
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .role(Role.USER)
                .build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        var token = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(token)
                .build();


    }

    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        System.out.println("EMAIL: " + request.getEmail());
        System.out.println("PASS RAW: " + request.getPassword());

        System.out.println("PASS HASH: " + user.getPassword());
        System.out.println("MATCHES: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));



        var token = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(token)
                .build();
    }
}
