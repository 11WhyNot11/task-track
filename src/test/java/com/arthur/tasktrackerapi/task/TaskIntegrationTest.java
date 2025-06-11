package com.arthur.tasktrackerapi.task;

import com.arthur.tasktrackerapi.IntegrationTest;
import com.arthur.tasktrackerapi.task.dto.TaskRequestDto;
import com.arthur.tasktrackerapi.task.dto.TaskResponseDto;
import com.arthur.tasktrackerapi.task.entity.Priority;
import com.arthur.tasktrackerapi.task.entity.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskIntegrationTest extends IntegrationTest {

    private String jwtToken;
    private Long projectId;

    @BeforeAll
    void init() {
       jwtToken =  registerAndGetToken(restTemplate, "task_test@gmail.com");
       projectId = createProject("task test", jwtToken);
    }

    @Test
    void shouldCreateTask() {
        //given
        var requestDto = TaskRequestDto.builder()
                .title("title test")
                .description("description test")
                .status(Status.TODO)
                .priority(Priority.HIGH)
                .deadline(LocalDateTime.now().plusHours(1))
                .projectId(projectId)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<TaskRequestDto> request = new HttpEntity<>(requestDto, headers);

        //when
        ResponseEntity<TaskResponseDto> responseDto = restTemplate.postForEntity(
                "/api/tasks",
                request,
                TaskResponseDto.class
        );

        //then
        assertThat(responseDto.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseDto.getBody()).isNotNull();
        assertThat(responseDto.getBody().getTitle()).isEqualTo("title test");
    }

}
