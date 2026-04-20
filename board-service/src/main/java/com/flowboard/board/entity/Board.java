package com.flowboard.board.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer boardId;

    @Column(nullable = false)
    private Integer workspaceId;

    @Column(nullable = false)
    private String name;

    private String description;

    private String background;

    @Column(nullable = false)
    private String visibility = "PUBLIC"; // PUBLIC, PRIVATE

    @Column(nullable = false)
    private Integer createdById;

    @Column(nullable = false)
    private Boolean isClosed = false;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}