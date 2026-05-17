package com.flowboard.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMemberDto {
    private Integer memberId;
    private Integer workspaceId;
    private Integer userId;
    private String role;
    private LocalDate joinedAt;
}
