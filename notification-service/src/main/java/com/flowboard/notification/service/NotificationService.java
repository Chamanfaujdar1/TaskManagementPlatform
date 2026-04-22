package com.flowboard.notification.service;

import com.flowboard.notification.entity.Notification;
import java.util.List;

public interface NotificationService {

    void send(Notification notification);

    void sendBulk(List<Integer> recipientIds,
                  String title,
                  String message);

    void markAsRead(int notificationId);

    void markAllRead(int recipientId);

    void deleteRead(int recipientId);

    List<Notification> getByRecipient(int recipientId);

    long getUnreadCount(int recipientId);

    void deleteNotification(int notificationId);

    List<Notification> getAll();
}