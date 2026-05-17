package com.flowboard.label;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowboard.label.dto.LabelDto;
import com.flowboard.label.repository.LabelRepository;
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
public class LabelResourceIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        labelRepository.deleteAll();
    }

    @Test
    void createAndGetLabels_Success() throws Exception {
        // 1. Create Label
        LabelDto label = new LabelDto();
        label.setName("Critical");
        label.setColor("#FF0000");
        label.setBoardId(1);

        mockMvc.perform(post("/api/v1/labels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(label)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Critical"));

        // 2. Get Labels by Board ID
        mockMvc.perform(get("/api/v1/labels/board/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Critical"));
    }
}
