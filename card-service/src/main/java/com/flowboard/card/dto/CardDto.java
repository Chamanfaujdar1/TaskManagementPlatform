package com.flowboard.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Integer cardId;
    private Integer listId;
    private Integer boardId;
    private String title;
    private String description;
    private Integer position;
    private String priority;
    private String status;
    private LocalDate dueDate;
    private LocalDate startDate;
    private Integer assigneeId;
    private Integer createdById;
    private Boolean isArchived;
    private Boolean oneDayReminderSent;
    private Boolean oneHourReminderSent;
    private String coverColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
