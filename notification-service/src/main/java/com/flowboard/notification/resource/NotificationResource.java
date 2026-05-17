package com.flowboard.notification.resource;

import com.flowboard.notification.dto.NotificationDto;
import com.flowboard.notification.model.RealTimeUpdate;
import com.flowboard.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationResource {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    // SEND NOTIFICATION
    @PostMapping
    public ResponseEntity<String> send(
            @RequestBody NotificationDto notificationDto) {
        notificationService.send(notificationDto);
        return ResponseEntity.ok(
                "Notification sent successfully");
    }

    // SEND BULK NOTIFICATION
    @PostMapping("/bulk")
    public ResponseEntity<String> sendBulk(
            @RequestBody Map<String, Object> request) {
        List<Integer> recipientIds =
                (List<Integer>) request.get("recipientIds");
        String title = (String) request.get("title");
        String message = (String) request.get("message");
        notificationService.sendBulk(
                recipientIds, title, message);
        return ResponseEntity.ok(
                "Bulk notifications sent successfully");
    }

    // GET BY RECIPIENT
    @GetMapping("/recipient/{userId}")
    public ResponseEntity<List<NotificationDto>> getByRecipient(
            @PathVariable int userId) {
        return ResponseEntity.ok(
                notificationService.getByRecipient(userId));
    }

    // GET UNREAD COUNT
    @GetMapping("/recipient/{userId}/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @PathVariable int userId) {
        long count =
                notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(
                Map.of("unreadCount", count));
    }

    // MARK AS READ
    @PutMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable int id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(
                "Notification marked as read");
    }

    // MARK ALL READ
    @PutMapping("/recipient/{userId}/read-all")
    public ResponseEntity<String> markAllRead(
            @PathVariable int userId) {
        notificationService.markAllRead(userId);
        return ResponseEntity.ok(
                "All notifications marked as read");
    }

    // DELETE READ NOTIFICATIONS
    @DeleteMapping("/read/{userId}")
    public ResponseEntity<String> deleteRead(
            @PathVariable int userId) {
        notificationService.deleteRead(userId);
        return ResponseEntity.ok(
                "Read notifications deleted");
    }

    // DELETE NOTIFICATION
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable int id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(
                "Notification deleted successfully");
    }

    // GET ALL NOTIFICATIONS
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAll() {
        return ResponseEntity.ok(
                notificationService.getAll());
    }

    // SEND EMAIL NOTIFICATION
    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(
            @RequestBody Map<String, Object> request) {
        int recipientId = (Integer) request.get("recipientId");
        String subject = (String) request.get("title");
        String message = (String) request.get("message");
        notificationService.sendEmail(recipientId, subject, message);
        return ResponseEntity.ok(
                "Email dispatch initiated successfully");
    }

    // BROADCAST REAL-TIME UPDATE
    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcast(
            @RequestBody RealTimeUpdate update) {
        messagingTemplate.convertAndSend(
                "/topic/board/" + update.getBoardId(),
                update);
        return ResponseEntity.ok(
                "Real-time update broadcasted successfully");
    }
}