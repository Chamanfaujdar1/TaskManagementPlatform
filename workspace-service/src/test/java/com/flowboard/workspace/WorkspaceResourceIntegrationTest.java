package com.flowboard.workspace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowboard.workspace.dto.WorkspaceDto;
import com.flowboard.workspace.repository.WorkspaceRepository;
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
public class WorkspaceResourceIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        workspaceRepository.deleteAll();
    }

    @Test
    void createAndGetWorkspace_Success() throws Exception {
        // 1. Create Workspace
        WorkspaceDto workspace = new WorkspaceDto();
        workspace.setName("Integration Workspace");
        workspace.setOwnerId(1);
        workspace.setVisibility("PUBLIC");

        String result = mockMvc.perform(post("/api/v1/workspaces")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workspace)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Workspace"))
                .andReturn().getResponse().getContentAsString();

        WorkspaceDto saved = objectMapper.readValue(result, WorkspaceDto.class);

        // 2. Get Workspace by ID
        mockMvc.perform(get("/api/v1/workspaces/" + saved.getWorkspaceId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Workspace"));
    }

    @Test
    void getById_NotFound_ThrowsException() throws Exception {
        mockMvc.perform(get("/api/v1/workspaces/999"))
                .andExpect(status().isNotFound());
    }
}
