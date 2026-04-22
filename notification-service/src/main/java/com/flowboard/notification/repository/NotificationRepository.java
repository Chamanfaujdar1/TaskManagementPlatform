package com.flowboard.notification.repository;

import com.flowboard.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Integer> {

    List<Notification> findByRecipientId(int recipientId);

    List<Notification> findByRecipientIdAndIsRead(
            int recipientId, Boolean isRead);

    long countByRecipientIdAndIsRead(
            int recipientId, Boolean isRead);

    List<Notification> findByType(String type);

    List<Notification> findByRelatedId(int relatedId);

    void deleteByNotificationId(int notificationId);

    void deleteByRecipientIdAndIsRead(
            int recipientId, Boolean isRead);
}