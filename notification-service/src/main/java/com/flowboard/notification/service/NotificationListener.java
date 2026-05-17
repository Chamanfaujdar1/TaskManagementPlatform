package com.flowboard.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.flowboard.notification.dto.NotificationDto;
import com.flowboard.notification.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
@org.springframework.context.annotation.Profile("!test")
public class NotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleNotification(com.flowboard.notification.model.NotificationEvent event) {
        log.info("Received notification event from RabbitMQ: {}", event);
        try {
            if (event.getRecipientId() == null) {
                log.warn("Received event with null recipientId. Skipping.");
                return;
            }
            
            NotificationDto notification = new NotificationDto();
            notification.setRecipientId(event.getRecipientId());
            notification.setActorId(event.getActorId() != null ? event.getActorId() : 0);
            notification.setType(event.getType());
            notification.setTitle(event.getTitle());
            notification.setMessage(event.getMessage());
            notification.setIsRead(false);
            
            notificationService.send(notification);
            log.info("Notification successfully processed and saved for user: {}", notification.getRecipientId());
        } catch (Exception e) {
            log.error("CRITICAL: Error processing notification from RabbitMQ: {}", e.getMessage(), e);
        }
    }
}
