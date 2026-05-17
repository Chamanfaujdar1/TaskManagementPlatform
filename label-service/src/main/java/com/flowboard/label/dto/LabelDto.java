package com.flowboard.label.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelDto {
    private Integer labelId;
    private Integer boardId;
    private String name;
    private String color;
    private LocalDate createdAt;
}
