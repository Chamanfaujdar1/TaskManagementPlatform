package com.flowboard.auth.service;

import com.flowboard.auth.config.JwtUtil;
import com.flowboard.auth.dto.UserDto;
import com.flowboard.auth.entity.User;
import com.flowboard.auth.exception.BadRequestException;
import com.flowboard.auth.repository.UserRepository;
import com.flowboard.auth.serviceimpl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setEmail("test@flowboard.com");
        testUser.setUsername("testuser");
        testUser.setPasswordHash("rawPassword");
        testUser.setFullName("Test User");

        testUserDto = new UserDto();
        testUserDto.setEmail("test@flowboard.com");
        testUserDto.setUsername("testuser");
        testUserDto.setPassword("rawPassword");
        testUserDto.setFullName("Test User");
    }

    @Test
    void register_Success() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserDto result = authService.register(testUserDto);

        // Assert
        assertNotNull(result);
        assertEquals("test@flowboard.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_EmailExists_ThrowsException() {
        // Arrange
        when(userRepository.existsByEmail("test@flowboard.com")).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> authService.register(testUserDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success_ReturnsToken() {
        // Arrange
        testUser.setPasswordHash("encodedPassword");
        testUser.setIsActive(true);
        testUser.setRole("MEMBER");

        when(userRepository.findByEmail("test@flowboard.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@flowboard.com", "MEMBER")).thenReturn("mock-jwt-token");

        // Act
        String token = authService.login("test@flowboard.com", "rawPassword");

        // Assert
        assertEquals("mock-jwt-token", token);
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        // Arrange
        testUser.setPasswordHash("encodedPassword");
        testUser.setIsActive(true);

        when(userRepository.findByEmail("test@flowboard.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> authService.login("test@flowboard.com", "wrongPassword"));
    }
}
