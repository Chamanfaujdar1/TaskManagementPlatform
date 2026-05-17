package com.flowboard.notification.serviceimpl;

import com.flowboard.notification.dto.NotificationDto;
import com.flowboard.notification.entity.Notification;
import com.flowboard.notification.exception.BadRequestException;
import com.flowboard.notification.exception.ResourceNotFoundException;
import com.flowboard.notification.mapper.NotificationMapper;
import com.flowboard.notification.repository.NotificationRepository;
import com.flowboard.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Override
    public void send(NotificationDto notificationDto) {
        Notification notification = NotificationMapper.mapToEntity(notificationDto);
        if (notification.getTitle() == null || notification.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Notification title cannot be empty");
        }
        notificationRepository.save(notification);
    }

    @Override
    public void sendBulk(List<Integer> recipientIds, String title, String message) {
        if (title == null || title.trim().isEmpty()) {
            throw new BadRequestException("Notification title cannot be empty");
        }

        List<Integer> targetIds = recipientIds;
        // If recipientIds is null or empty, it's a platform-wide broadcast
        if (targetIds == null || targetIds.isEmpty()) {
            targetIds = getAllUserIds();
        }

        if (targetIds.isEmpty()) {
            throw new BadRequestException("No recipients found for broadcast");
        }

        for (int recipientId : targetIds) {
            Notification notification = new Notification();
            notification.setRecipientId(recipientId);
            notification.setActorId(0);
            notification.setType("BROADCAST");
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setIsRead(false);
            notificationRepository.save(notification);
        }
    }

    private List<Integer> getAllUserIds() {
        try {
            String url = authServiceUrl + "/api/v1/auth/users";
            List<Map<String, Object>> users = restTemplate.getForObject(url, List.class);
            if (users == null || users.isEmpty()) return List.of();
            
            return users.stream()
                    .map(u -> (Integer) u.get("userId"))
                    .toList();
        } catch (Exception e) {
            log.error("Failed to fetch users for broadcast: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public void markAsRead(int notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found with id: " + notificationId));
        if (notification.getIsRead()) {
            throw new BadRequestException("Notification is already marked as read");
        }
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllRead(int recipientId) {
        List<Notification> unread = notificationRepository.findByRecipientIdAndIsRead(recipientId, false);
        for (Notification n : unread) {
            n.setIsRead(true);
            notificationRepository.save(n);
        }
    }

    @Override
    @Transactional
    public void deleteRead(int recipientId) {
        notificationRepository.deleteByRecipientIdAndIsRead(recipientId, true);
    }

    @Override
    public List<NotificationDto> getByRecipient(int recipientId) {
        return notificationRepository.findByRecipientId(recipientId).stream()
                .map(NotificationMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getUnreadCount(int recipientId) {
        return notificationRepository.countByRecipientIdAndIsRead(recipientId, false);
    }

    @Override
    @Transactional
    public void deleteNotification(int notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new ResourceNotFoundException("Notification not found with id: " + notificationId);
        }
        notificationRepository.deleteByNotificationId(notificationId);
    }

    @Override
    public List<NotificationDto> getAll() {
        return notificationRepository.findAll().stream()
                .map(NotificationMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void sendEmail(int recipientId, String subject, String text) {
        try {
            // Fetch user profile from auth-service
            String url = authServiceUrl + "/api/v1/auth/profile/" + recipientId;
            Map<String, Object> userProfile = restTemplate.getForObject(url, Map.class);
            if (userProfile != null && userProfile.containsKey("email")) {
                String email = (String) userProfile.get("email");
                
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject(subject);
                message.setText(text);
                message.setFrom("noreply@flowboard.com");
                
                mailSender.send(message);
            }
        } catch (Exception e) {
            // Log error or handle gracefully
            log.error("Failed to send email to recipient {}: {}", recipientId, e.getMessage());
        }
    }
}