package com.flowboard.comment.resource;

import com.flowboard.comment.entity.Attachment;
import com.flowboard.comment.entity.Comment;
import com.flowboard.comment.service.CommentService;
import com.flowboard.comment.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CommentResource {

    @Autowired
    private CommentService commentService;

    @Autowired
    private S3Service s3Service;

    // ADD COMMENT
    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(
            @RequestBody Comment comment) {
        return ResponseEntity.ok(
                commentService.addComment(comment));
    }

    // GET COMMENTS BY CARD
    @GetMapping("/comments/card/{cardId}")
    public ResponseEntity<List<Comment>> getByCard(
            @PathVariable int cardId) {
        return ResponseEntity.ok(
                commentService.getByCard(cardId));
    }

    // GET COMMENT BY ID
    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getById(
            @PathVariable int id) {
        return ResponseEntity.ok(
                commentService.getCommentById(id));
    }

    // GET REPLIES
    @GetMapping("/comments/{id}/replies")
    public ResponseEntity<List<Comment>> getReplies(
            @PathVariable int id) {
        return ResponseEntity.ok(
                commentService.getReplies(id));
    }

    // UPDATE COMMENT
    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> update(
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
    public ResponseEntity<Attachment> addAttachment(
            @RequestBody Attachment attachment) {
        return ResponseEntity.ok(
                commentService.addAttachment(attachment));
    }

    // GET ATTACHMENTS BY CARD
    @GetMapping("/attachments/card/{cardId}")
    public ResponseEntity<List<Attachment>> getAttachments(
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
    public ResponseEntity<Attachment> uploadAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("cardId") int cardId,
            @RequestParam("uploaderId") int uploaderId) {
        try {
            String fileUrl = s3Service.uploadFile(file);
            Attachment attachment = new Attachment();
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