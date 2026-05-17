package com.flowboard.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {
    private Integer attachmentId;
    private Integer cardId;
    private Integer uploaderId;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long sizeKb;
    private LocalDateTime uploadedAt;
}
