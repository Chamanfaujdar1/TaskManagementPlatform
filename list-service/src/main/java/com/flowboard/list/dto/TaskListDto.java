package com.flowboard.list.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskListDto {
    private Integer listId;
    private Integer boardId;
    private String name;
    private Integer position;
    private String color;
    private Boolean isArchived;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
