package com.flowboard.label.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemDto {
    private Integer itemId;
    private Integer checklistId;
    private String text;
    private Boolean isCompleted;
    private Integer assigneeId;
    private LocalDate dueDate;
}
