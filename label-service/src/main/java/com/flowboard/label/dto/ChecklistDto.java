package com.flowboard.label.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistDto {
    private Integer checklistId;
    private Integer cardId;
    private String title;
    private Integer position;
    private LocalDateTime createdAt;
}
