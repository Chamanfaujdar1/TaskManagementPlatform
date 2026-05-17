package com.flowboard.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardMemberDto {
    private Integer boardMemberId;
    private Integer boardId;
    private Integer userId;
    private String role;
    private LocalDate addedAt;
}
