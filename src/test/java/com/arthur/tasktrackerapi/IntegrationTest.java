package com.arthur.tasktrackerapi;

import com.arthur.tasktrackerapi.security.dto.AuthenticationResponseDto;
import com.arthur.tasktrackerapi.task.dto.TaskRequestDto;
import com.arthur.tasktrackerapi.task.dto.TaskResponseDto;
import com.arthur.tasktrackerapi.task.entity.Priority;
import com.arthur.tasktrackerapi.task.entity.Status;
import com.arthur.tasktrackerapi.user.dto.UserRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public abstract class IntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    protected static String registerAndGetToken(TestRestTemplate restTemplate, String email) {
        UserRequestDto userDto = UserRequestDto.builder()
                .email(email)
                .password("12345678")
                .firstName("Test")
                .lastName("User")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRequestDto> registerRequest = new HttpEntity<>(userDto, headers);

        ResponseEntity<AuthenticationResponseDto> response = restTemplate.postForEntity(
                "/api/auth/register",
                registerRequest,
                AuthenticationResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return response.getBody().getToken();
    }

    protected Long createProject(String name, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(
                Map.of(
                        "name", name,
                        "description", "Created by test"
                ),
                headers
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/projects",
                request,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return ((Number) response.getBody().get("id")).longValue();
    }

    protected Long createTask(Long projectId, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        TaskRequestDto dto = TaskRequestDto.builder()
                .title("Test Task")
                .description("Created for comment testing")
                .status(Status.TODO)
                .priority(Priority.MEDIUM)
                .deadline(LocalDateTime.of(2099, 1, 1, 0, 0))
                .projectId(projectId)
                .build();

        HttpEntity<TaskRequestDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/tasks",
                request,
                String.class
        );

        if (!response.getStatusCode().equals(HttpStatus.CREATED)) {
            System.out.println(" Failed to create task. Response:");
            System.out.println(response.getBody());
            throw new RuntimeException("Expected 201 CREATED but got: " + response.getStatusCode());
        }

        try {
            TaskResponseDto taskResponse = objectMapper.readValue(response.getBody(), TaskResponseDto.class);
            return taskResponse.getId();
        } catch (Exception e) {
            System.out.println("JSON parse failed: " + response.getBody());
            throw new RuntimeException("Failed to deserialize TaskResponseDto: " + e.getMessage(), e);
        }
    }

}


