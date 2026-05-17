package com.flowboard.comment.service;

import com.flowboard.comment.dto.CommentDto;
import com.flowboard.comment.entity.Comment;
import com.flowboard.comment.exception.BadRequestException;
import com.flowboard.comment.repository.AttachmentRepository;
import com.flowboard.comment.repository.CommentRepository;
import com.flowboard.comment.serviceimpl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment testComment;
    private CommentDto testCommentDto;

    @BeforeEach
    void setUp() {
        testComment = new Comment();
        testComment.setCommentId(1);
        testComment.setContent("Test Comment");
        testComment.setCardId(10);
        testComment.setAuthorId(100);
        testComment.setIsDeleted(false);

        testCommentDto = new CommentDto();
        testCommentDto.setCommentId(1);
        testCommentDto.setContent("Test Comment");
        testCommentDto.setCardId(10);
        testCommentDto.setAuthorId(100);
    }

    @Test
    void addComment_Success() {
        // Arrange
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // Act
        CommentDto saved = commentService.addComment(testCommentDto);

        // Assert
        assertNotNull(saved);
        assertFalse(saved.getIsDeleted());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addComment_EmptyContent_ThrowsException() {
        // Arrange
        testCommentDto.setContent("");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> commentService.addComment(testCommentDto));
    }
}
