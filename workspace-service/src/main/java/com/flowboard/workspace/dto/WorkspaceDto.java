package com.flowboard.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceDto {
    private Integer workspaceId;
    private String name;
    private String description;
    private Integer ownerId;
    private String visibility;
    private String logoUrl;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
