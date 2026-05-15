package com.flowboard.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeUpdate {
    private String type; // e.g., CARD_MOVED, CARD_UPDATED, LIST_REORDERED
    private Integer boardId;
    private Map<String, Object> payload;
}
