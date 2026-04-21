package com.flowboard.label.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "labels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer labelId;

    @Column(nullable = false)
    private Integer boardId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color; // hex e.g. #FF5733

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}
