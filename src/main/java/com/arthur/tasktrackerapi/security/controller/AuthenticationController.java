package com.arthur.tasktrackerapi.security.controller;

import com.arthur.tasktrackerapi.security.dto.AuthenticationRequestDto;
import com.arthur.tasktrackerapi.security.dto.AuthenticationResponseDto;
import com.arthur.tasktrackerapi.security.service.AuthenticationService;
import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody UserRequestDto userRequestDto){
        AuthenticationResponseDto response = authenticationService.register(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthenticationRequestDto authenticationRequestDto){
        var response = authenticationService.login(authenticationRequestDto);
        return ResponseEntity.ok(response);
    }

}
