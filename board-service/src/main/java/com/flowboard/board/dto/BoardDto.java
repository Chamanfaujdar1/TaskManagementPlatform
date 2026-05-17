package com.flowboard.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Integer boardId;
    private Integer workspaceId;
    private String name;
    private String description;
    private String background;
    private String visibility;
    private Integer createdById;
    private Boolean isClosed;
    private LocalDate createdAt;
}
