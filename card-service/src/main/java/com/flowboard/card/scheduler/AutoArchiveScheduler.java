package com.flowboard.card.scheduler;

import com.flowboard.card.entity.Card;
import com.flowboard.card.repository.CardRepository;
import com.flowboard.card.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AutoArchiveScheduler {

    private final CardRepository cardRepository;
    private final CardService cardService;

    // Run at 2:00 AM every day
    @Scheduled(cron = "0 0 2 * * ?")
    public void autoArchiveOverdueCards() {
        log.info("Running auto-archive overdue cards job...");

        // Fetch non-archived cards that are overdue (dueDate < today and status != "DONE")
        List<Card> overdueCards = cardRepository.findByDueDateBeforeAndStatusNotAndIsArchivedFalse(LocalDate.now(), "DONE");

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        for (Card card : overdueCards) {
            // Check if there's been no activity in the last 7 days
            if (card.getUpdatedAt() != null && card.getUpdatedAt().isBefore(sevenDaysAgo)) {
                log.info("Auto-archiving card with ID: {} (no activity for 7 days)", card.getCardId());
                try {
                    cardService.archiveCard(card.getCardId());
                } catch (Exception e) {
                    log.error("Failed to auto-archive card {}: {}", card.getCardId(), e.getMessage());
                }
            }
        }
    }
}
