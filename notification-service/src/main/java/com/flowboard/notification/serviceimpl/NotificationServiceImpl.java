package com.flowboard.notification.serviceimpl;

import com.flowboard.notification.entity.Notification;
import com.flowboard.notification.repository.NotificationRepository;
import com.flowboard.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationServiceImpl
        implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void send(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void sendBulk(List<Integer> recipientIds,
                         String title,
                         String message) {
        for (int recipientId : recipientIds) {
            Notification notification = new Notification();
            notification.setRecipientId(recipientId);
            notification.setActorId(0); // system
            notification.setType("BROADCAST");
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setIsRead(false);
            notificationRepository.save(notification);
        }
    }

    @Override
    public void markAsRead(int notificationId) {
        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllRead(int recipientId) {
        List<Notification> unread = notificationRepository
                .findByRecipientIdAndIsRead(
                        recipientId, false);
        for (Notification n : unread) {
            n.setIsRead(true);
            notificationRepository.save(n);
        }
    }

    @Override
    @Transactional
    public void deleteRead(int recipientId) {
        notificationRepository
                .deleteByRecipientIdAndIsRead(
                        recipientId, true);
    }

    @Override
    public List<Notification> getByRecipient(
            int recipientId) {
        return notificationRepository
                .findByRecipientId(recipientId);
    }

    @Override
    public long getUnreadCount(int recipientId) {
        return notificationRepository
                .countByRecipientIdAndIsRead(
                        recipientId, false);
    }

    @Override
    @Transactional
    public void deleteNotification(int notificationId) {
        notificationRepository
                .deleteByNotificationId(notificationId);
    }

    @Override
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }
}