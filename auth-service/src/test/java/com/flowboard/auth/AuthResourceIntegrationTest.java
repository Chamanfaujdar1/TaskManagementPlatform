package com.flowboard.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowboard.auth.entity.User;
import com.flowboard.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class AuthResourceIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
    }

    @Test
    void registerAndLogin_Success() throws Exception {
        // 1. Register a new user
        User user = new User();
        user.setFullName("Integration Test");
        user.setUsername("inttest");
        user.setEmail("int@test.com");
        user.setPasswordHash("password123");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("int@test.com"));

        // 2. Try to Login with the registered user
        Map<String, String> loginReq = new HashMap<>();
        loginReq.put("email", "int@test.com");
        loginReq.put("password", "password123");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("."))); 
    }

    @Test
    void login_Failure_WrongPassword() throws Exception {
        // 1. Create a user manually in DB
        User user = new User();
        user.setFullName("User");
        user.setUsername("user");
        user.setEmail("user@test.com");
        user.setPasswordHash(passwordEncoder.encode("correctPass"));
        user.setRole("MEMBER");
        user.setIsActive(true);
        userRepository.save(user);

        // 2. Login with wrong password
        Map<String, String> loginReq = new HashMap<>();
        loginReq.put("email", "user@test.com");
        loginReq.put("password", "wrongPass");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isBadRequest());
    }
}
