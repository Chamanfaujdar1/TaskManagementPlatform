package com.flowboard.card.scheduler;

import com.flowboard.card.entity.Card;
import com.flowboard.card.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DueDateReminderScheduler {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    // Run every hour
    @Scheduled(cron = "0 0 * * * ?")
    public void checkAndSendReminders() {
        System.out.println("Running due date reminders job...");
        
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalTime now = LocalTime.now();

        // 1. One Day Reminders (Cards due tomorrow)
        List<Card> dueTomorrow = cardRepository.findByDueDateAndStatusNotAndIsArchivedFalse(tomorrow, "DONE");
        for (Card card : dueTomorrow) {
            if (card.getOneDayReminderSent() != null && !card.getOneDayReminderSent() && card.getAssigneeId() != null) {
                sendNotification(card.getAssigneeId(), 
                        "Upcoming Due Date: " + card.getTitle(),
                        "The card '" + card.getTitle() + "' is due tomorrow.");
                card.setOneDayReminderSent(true);
                cardRepository.save(card);
                System.out.println("Sent 1-day reminder for card: " + card.getCardId());
            }
        }

        // 2. One Hour Reminders (Cards due today, assuming due time is 23:59)
        // If current hour is 22 (10:00 PM to 11:00 PM window), send the 1-hour reminder
        if (now.getHour() == 22) {
            List<Card> dueToday = cardRepository.findByDueDateAndStatusNotAndIsArchivedFalse(today, "DONE");
            for (Card card : dueToday) {
                if (card.getOneHourReminderSent() != null && !card.getOneHourReminderSent() && card.getAssigneeId() != null) {
                    sendNotification(card.getAssigneeId(),
                            "Urgent: Due in 1 Hour - " + card.getTitle(),
                            "The card '" + card.getTitle() + "' is due in approximately 1 hour.");
                    card.setOneHourReminderSent(true);
                    cardRepository.save(card);
                    System.out.println("Sent 1-hour reminder for card: " + card.getCardId());
                }
            }
        }
    }

    private void sendNotification(Integer recipientId, String title, String message) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("recipientId", recipientId);
            request.put("actorId", 0); // 0 indicates system generated
            request.put("type", "REMINDER");
            request.put("title", title);
            request.put("message", message);
            request.put("isRead", false);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            restTemplate.postForEntity(notificationServiceUrl + "/api/v1/notifications", entity, String.class);
        } catch (Exception e) {
            System.err.println("Failed to send reminder notification for recipient " + recipientId + ": " + e.getMessage());
        }
    }
}
