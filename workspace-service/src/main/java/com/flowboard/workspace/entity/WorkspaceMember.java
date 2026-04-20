package com.flowboard.workspace.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "workspace_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberId;

    @Column(nullable = false)
    private Integer workspaceId;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String role = "MEMBER"; // ADMIN, MEMBER

    @Column(nullable = false, updatable = false)
    private LocalDate joinedAt;

    @PrePersist
    protected void onCreate() {
        this.joinedAt = LocalDate.now();
    }
}