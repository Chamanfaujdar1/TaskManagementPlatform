package com.flowboard.notification.service;

import com.flowboard.notification.entity.Notification;
import com.flowboard.notification.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@org.springframework.context.annotation.Profile("!test")
public class NotificationListener {

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleNotification(com.flowboard.notification.model.NotificationEvent event) {
        System.out.println("Received notification event from RabbitMQ: " + event);
        try {
            if (event.getRecipientId() == null) {
                System.err.println("Received event with null recipientId. Skipping.");
                return;
            }
            
            Notification notification = new Notification();
            notification.setRecipientId(event.getRecipientId());
            notification.setActorId(event.getActorId() != null ? event.getActorId() : 0);
            notification.setType(event.getType());
            notification.setTitle(event.getTitle());
            notification.setMessage(event.getMessage());
            notification.setIsRead(false);
            
            notificationService.send(notification);
            System.out.println("Notification successfully processed and saved for user: " + notification.getRecipientId());
        } catch (Exception e) {
            System.err.println("CRITICAL: Error processing notification from RabbitMQ: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
