package com.arthur.tasktrackerapi.project;

import com.arthur.tasktrackerapi.IntegrationTest;
import com.arthur.tasktrackerapi.project.dto.ProjectRequestDto;
import com.arthur.tasktrackerapi.project.dto.ProjectResponseDto;
import com.arthur.tasktrackerapi.security.dto.AuthenticationResponseDto;
import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectIntegrationTest extends IntegrationTest {
    
    private static String jwtToken;


    @BeforeAll
    void init(@Autowired TestRestTemplate restTemplate) {
        jwtToken = registerAndGetToken(restTemplate, "task_test@gmail.com");
    }

    @Test
    void shouldCreateProject() {
        //given
        ProjectRequestDto dto = ProjectRequestDto.builder()
                .name("test")
                .description("test description")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<ProjectRequestDto> request = new HttpEntity<>(dto, headers);

        //when
        ResponseEntity<ProjectResponseDto> response = restTemplate.postForEntity(
                "/api/projects",
                request,
                ProjectResponseDto.class
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("test");



    }

}
