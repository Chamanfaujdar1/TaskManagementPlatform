package com.flowboard.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowboard.board.dto.BoardDto;
import com.flowboard.board.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class BoardResourceIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        boardRepository.deleteAll();
    }

    @Test
    void createAndGetBoard_Success() throws Exception {
        // 1. Create Board
        BoardDto board = new BoardDto();
        board.setName("Integration Board");
        board.setWorkspaceId(1);
        board.setCreatedById(100);
        board.setVisibility("WORKSPACE");

        String result = mockMvc.perform(post("/api/v1/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(board)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Board"))
                .andReturn().getResponse().getContentAsString();

        BoardDto saved = objectMapper.readValue(result, BoardDto.class);

        // 2. Get Board by ID
        mockMvc.perform(get("/api/v1/boards/" + saved.getBoardId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Board"));
    }
}
