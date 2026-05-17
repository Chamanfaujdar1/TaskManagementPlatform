package com.flowboard.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowboard.comment.dto.CommentDto;
import com.flowboard.comment.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.flowboard.comment.service.S3Service;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class CommentResourceIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        commentRepository.deleteAll();
    }

    @Test
    void addAndGetComments_Success() throws Exception {
        // 1. Add Comment
        CommentDto comment = new CommentDto();
        comment.setContent("Integration Comment");
        comment.setCardId(1);
        comment.setAuthorId(100);

        mockMvc.perform(post("/api/v1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Integration Comment"));

        // 2. Get Comments by Card ID
        mockMvc.perform(get("/api/v1/comments/card/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Integration Comment"));
    }
}
