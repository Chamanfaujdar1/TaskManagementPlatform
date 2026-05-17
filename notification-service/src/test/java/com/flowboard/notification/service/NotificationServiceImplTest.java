package com.flowboard.notification.service;

import com.flowboard.notification.dto.NotificationDto;
import com.flowboard.notification.entity.Notification;
import com.flowboard.notification.exception.BadRequestException;
import com.flowboard.notification.repository.NotificationRepository;
import com.flowboard.notification.serviceimpl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification testNotification;
    private NotificationDto testNotificationDto;

    @BeforeEach
    void setUp() {
        testNotification = new Notification();
        testNotification.setNotificationId(1);
        testNotification.setTitle("Test Title");
        testNotification.setMessage("Test Message");
        testNotification.setRecipientId(100);
        testNotification.setIsRead(false);

        testNotificationDto = new NotificationDto();
        testNotificationDto.setNotificationId(1);
        testNotificationDto.setTitle("Test Title");
        testNotificationDto.setMessage("Test Message");
        testNotificationDto.setRecipientId(100);
        testNotificationDto.setIsRead(false);
    }

    @Test
    void send_Success() {
        // Arrange
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        // Act
        notificationService.send(testNotificationDto);

        // Assert
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void send_EmptyTitle_ThrowsException() {
        // Arrange
        testNotificationDto.setTitle("");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> notificationService.send(testNotificationDto));
    }
}
