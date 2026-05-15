package com.flowboard.list;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowboard.list.entity.TaskList;
import com.flowboard.list.repository.ListRepository;
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
public class ListResourceIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        listRepository.deleteAll();
    }

    @Test
    void createAndGetLists_Success() throws Exception {
        // 1. Create List
        TaskList list = new TaskList();
        list.setName("Integration List");
        list.setBoardId(1);

        mockMvc.perform(post("/api/v1/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration List"));

        // 2. Get Lists by Board ID
        mockMvc.perform(get("/api/v1/lists/board/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Integration List"));
    }
}
