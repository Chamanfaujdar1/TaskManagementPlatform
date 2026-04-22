package com.flowboard.label.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "card_labels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer cardId;

    @Column(nullable = false)
    private Integer labelId;
}