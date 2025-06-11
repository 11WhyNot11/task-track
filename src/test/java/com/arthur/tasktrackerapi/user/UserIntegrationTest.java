package com.arthur.tasktrackerapi.user;

import com.arthur.tasktrackerapi.IntegrationTest;
import com.arthur.tasktrackerapi.security.dto.AuthenticationResponseDto;
import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import com.arthur.tasktrackerapi.user.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class UserIntegrationTest extends IntegrationTest {

    @Test
    void shouldRegisterAndReturnUserProfile() {
        //given
        UserRequestDto requestDto = UserRequestDto.builder()
                .email("test@gmail.com")
                .password("1234")
                .firstName("test")
                .lastName("name")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRequestDto> request = new HttpEntity<>(requestDto, headers);

        // when
        ResponseEntity<AuthenticationResponseDto> registerResponse = restTemplate.postForEntity(
                "/api/auth/register",
                request,
                AuthenticationResponseDto.class
        );

        System.out.println("Register status: " + registerResponse.getStatusCode());
        System.out.println("Register body: " + registerResponse.getBody());

        if (registerResponse.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("Register failed with status: " + registerResponse.getStatusCode());
            System.out.println("Response body: " + registerResponse.getBody());
            fail("Register failed with status: " + registerResponse.getStatusCode());
        }

        //then
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String token = registerResponse.getBody().getToken();
        assertThat(token).isNotBlank();


        //when
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(token);
        HttpEntity<Void> authRequest = new HttpEntity<>(authHeaders);

        ResponseEntity<UserResponseDto> meResponse = restTemplate.exchange(
                "/api/users/me",
                HttpMethod.GET,
                authRequest,
                UserResponseDto.class
        );

        System.out.println("me status: " + meResponse.getStatusCode());
        System.out.println("me body: " + meResponse.getBody());

        if (meResponse.getStatusCode().is5xxServerError()) {
            System.out.println("⚠️ SERVER ERROR RESPONSE:");
            System.out.println(meResponse);
        }

        //then
        assertThat(meResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meResponse.getBody().getEmail()).isEqualTo("test@gmail.com");

    }
}
