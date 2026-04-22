package com.flowboard.label.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "checklist_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @Column(nullable = false)
    private Integer checklistId;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    private Integer assigneeId;

    private LocalDate dueDate;
}