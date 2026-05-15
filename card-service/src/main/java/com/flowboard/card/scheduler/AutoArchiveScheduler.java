package com.flowboard.card.scheduler;

import com.flowboard.card.entity.Card;
import com.flowboard.card.repository.CardRepository;
import com.flowboard.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AutoArchiveScheduler {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    // Run at 2:00 AM every day
    @Scheduled(cron = "0 0 2 * * ?")
    public void autoArchiveOverdueCards() {
        System.out.println("Running auto-archive overdue cards job...");

        // Fetch non-archived cards that are overdue (dueDate < today and status != "DONE")
        List<Card> overdueCards = cardRepository.findByDueDateBeforeAndStatusNotAndIsArchivedFalse(LocalDate.now(), "DONE");

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        for (Card card : overdueCards) {
            // Check if there's been no activity in the last 7 days
            if (card.getUpdatedAt() != null && card.getUpdatedAt().isBefore(sevenDaysAgo)) {
                System.out.println("Auto-archiving card with ID: " + card.getCardId() + " (no activity for 7 days)");
                try {
                    cardService.archiveCard(card.getCardId());
                } catch (Exception e) {
                    System.err.println("Failed to auto-archive card " + card.getCardId() + ": " + e.getMessage());
                }
            }
        }
    }
}
