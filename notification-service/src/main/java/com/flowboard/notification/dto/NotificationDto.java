package com.flowboard.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Integer notificationId;
    private Integer recipientId;
    private Integer actorId;
    private String type;
    private String message;
    private String title;
    private Integer relatedId;
    private String relatedType;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
