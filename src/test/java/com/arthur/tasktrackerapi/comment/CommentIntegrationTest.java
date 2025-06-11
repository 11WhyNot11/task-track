package com.arthur.tasktrackerapi.comment;

import com.arthur.tasktrackerapi.IntegrationTest;
import com.arthur.tasktrackerapi.comment.dto.CommentRequestDto;
import com.arthur.tasktrackerapi.comment.dto.CommentResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentIntegrationTest extends IntegrationTest {

    private String jwtToken;
    private Long userId;
    private Long projectId;
    private Long taskId;

    @BeforeAll
    void init() {
        jwtToken = registerAndGetToken(restTemplate, "comment_test@gmail.com");
        projectId = createProject("Test Project", jwtToken);
        taskId = createTask(projectId, jwtToken);
    }

    @Test
    void shouldCreateComment() {
        CommentRequestDto requestDto = CommentRequestDto.builder()
                .content("test content")
                .taskId(taskId)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<CommentRequestDto> request = new HttpEntity<>(requestDto, headers);

        ResponseEntity<CommentResponseDto> response = restTemplate.postForEntity(
                "/api/comments",
                request,
                CommentResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).isEqualTo("test content");
    }
}
