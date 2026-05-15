package com.flowboard.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowboard.notification.entity.Notification;
import com.flowboard.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class NotificationResourceIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JavaMailSender mailSender;

    @MockitoBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        notificationRepository.deleteAll();
    }

    @Test
    void sendAndGetNotifications_Success() throws Exception {
        // 1. Send Notification
        Notification notification = new Notification();
        notification.setTitle("Integration Notification");
        notification.setMessage("Test Message");
        notification.setRecipientId(1);
        notification.setActorId(0);
        notification.setType("INFO");

        mockMvc.perform(post("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isOk());

        // 2. Get Notifications by Recipient ID
        mockMvc.perform(get("/api/v1/notifications/recipient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Integration Notification"));
    }
}
