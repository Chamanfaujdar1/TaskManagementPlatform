package com.flowboard.label.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardLabelDto {
    private Integer id;
    private Integer cardId;
    private Integer labelId;
}
