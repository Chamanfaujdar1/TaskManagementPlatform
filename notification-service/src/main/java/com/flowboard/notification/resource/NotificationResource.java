package com.flowboard.notification.resource;

import com.flowboard.notification.entity.Notification;
import com.flowboard.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationResource {

    @Autowired
    private NotificationService notificationService;

    // SEND NOTIFICATION
    @PostMapping
    public ResponseEntity<String> send(
            @RequestBody Notification notification) {
        notificationService.send(notification);
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
    public ResponseEntity<List<Notification>> getByRecipient(
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
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(
                notificationService.getAll());
    }
}