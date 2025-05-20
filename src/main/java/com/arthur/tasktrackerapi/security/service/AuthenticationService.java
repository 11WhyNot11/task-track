package com.arthur.tasktrackerapi.security.service;

import com.arthur.tasktrackerapi.security.dto.AuthenticationRequestDto;
import com.arthur.tasktrackerapi.security.dto.AuthenticationResponseDto;
import com.arthur.tasktrackerapi.user.dto.UserRequestDto;

public interface AuthenticationService {

    AuthenticationResponseDto register(UserRequestDto userRequestDto);

    AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto);



}
