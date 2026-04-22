package com.flowboard.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    @Column(nullable = false)
    private Integer recipientId;

    @Column(nullable = false)
    private Integer actorId;

    @Column(nullable = false)
    private String type;
    // ASSIGNMENT, MENTION, DUE_DATE, COMMENT, MOVE

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String title;

    private Integer relatedId;

    private String relatedType; // CARD, BOARD

    @Column(nullable = false)
    private Boolean isRead = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }
}