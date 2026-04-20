package com.flowboard.board.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "board_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer boardMemberId;

    @Column(nullable = false)
    private Integer boardId;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String role = "MEMBER"; // OBSERVER, MEMBER, ADMIN

    @Column(nullable = false, updatable = false)
    private LocalDate addedAt;

    @PrePersist
    protected void onCreate() {
        this.addedAt = LocalDate.now();
    }
}