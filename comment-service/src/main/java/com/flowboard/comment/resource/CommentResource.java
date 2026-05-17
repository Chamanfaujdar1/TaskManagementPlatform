package com.flowboard.comment.resource;

import com.flowboard.comment.dto.AttachmentDto;
import com.flowboard.comment.dto.CommentDto;
import com.flowboard.comment.service.CommentService;
import com.flowboard.comment.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentResource {

    private final CommentService commentService;
    private final S3Service s3Service;

    // ADD COMMENT
    @PostMapping("/comments")
    public ResponseEntity<CommentDto> addComment(
            @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(
                commentService.addComment(commentDto));
    }

    // GET COMMENTS BY CARD
    @GetMapping("/comments/card/{cardId}")
    public ResponseEntity<List<CommentDto>> getByCard(
            @PathVariable int cardId) {
        return ResponseEntity.ok(
                commentService.getByCard(cardId));
    }

    // GET COMMENT BY ID
    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentDto> getById(
            @PathVariable int id) {
        return ResponseEntity.ok(
                commentService.getCommentById(id));
    }

    // GET REPLIES
    @GetMapping("/comments/{id}/replies")
    public ResponseEntity<List<CommentDto>> getReplies(
            @PathVariable int id) {
        return ResponseEntity.ok(
                commentService.getReplies(id));
    }

    // UPDATE COMMENT
    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentDto> update(
            @PathVariable int id,
            @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(
                commentService.updateComment(
                        id, request.get("content")));
    }

    // DELETE COMMENT (soft delete)
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> delete(
            @PathVariable int id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(
                "Comment deleted successfully");
    }

    // GET COMMENT COUNT
    @GetMapping("/comments/card/{cardId}/count")
    public ResponseEntity<Long> getCount(
            @PathVariable int cardId) {
        return ResponseEntity.ok(
                commentService.getCommentCount(cardId));
    }

    // ADD ATTACHMENT
    @PostMapping("/attachments")
    public ResponseEntity<AttachmentDto> addAttachment(
            @RequestBody AttachmentDto attachmentDto) {
        return ResponseEntity.ok(
                commentService.addAttachment(attachmentDto));
    }

    // GET ATTACHMENTS BY CARD
    @GetMapping("/attachments/card/{cardId}")
    public ResponseEntity<List<AttachmentDto>> getAttachments(
            @PathVariable int cardId) {
        return ResponseEntity.ok(
                commentService.getAttachmentsByCard(cardId));
    }

    // DELETE ATTACHMENT
    @DeleteMapping("/attachments/{id}")
    public ResponseEntity<String> deleteAttachment(
            @PathVariable int id) {
        commentService.deleteAttachment(id);
        return ResponseEntity.ok(
                "Attachment deleted successfully");
    }

    // UPLOAD ATTACHMENT TO S3
    @PostMapping(value = "/attachments/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentDto> uploadAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("cardId") int cardId,
            @RequestParam("uploaderId") int uploaderId) {
        try {
            String fileUrl = s3Service.uploadFile(file);
            AttachmentDto attachment = new AttachmentDto();
            attachment.setCardId(cardId);
            attachment.setUploaderId(uploaderId);
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileUrl(fileUrl);
            attachment.setFileType(file.getContentType());
            attachment.setSizeKb(file.getSize() / 1024);
            
            return ResponseEntity.ok(commentService.addAttachment(attachment));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}