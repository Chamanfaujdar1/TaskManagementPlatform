package com.flowboard.comment.resource;

import com.flowboard.comment.entity.Attachment;
import com.flowboard.comment.entity.Comment;
import com.flowboard.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CommentResource {

    @Autowired
    private CommentService commentService;

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
}