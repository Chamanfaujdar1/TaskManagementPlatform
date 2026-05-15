package com.flowboard.label.service;

import com.flowboard.label.entity.Label;
import com.flowboard.label.exception.BadRequestException;
import com.flowboard.label.repository.CardLabelRepository;
import com.flowboard.label.repository.ChecklistItemRepository;
import com.flowboard.label.repository.ChecklistRepository;
import com.flowboard.label.repository.LabelRepository;
import com.flowboard.label.serviceimpl.LabelServiceImpl;
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
public class LabelServiceImplTest {

    @Mock private LabelRepository labelRepository;
    @Mock private CardLabelRepository cardLabelRepository;
    @Mock private ChecklistRepository checklistRepository;
    @Mock private ChecklistItemRepository checklistItemRepository;

    @InjectMocks
    private LabelServiceImpl labelService;

    private Label testLabel;

    @BeforeEach
    void setUp() {
        testLabel = new Label();
        testLabel.setLabelId(1);
        testLabel.setName("Bug");
        testLabel.setColor("#FF0000");
        testLabel.setBoardId(10);
    }

    @Test
    void createLabel_Success() {
        // Arrange
        when(labelRepository.save(any(Label.class))).thenReturn(testLabel);

        // Act
        Label saved = labelService.createLabel(testLabel);

        // Assert
        assertNotNull(saved);
        assertEquals("Bug", saved.getName());
        verify(labelRepository, times(1)).save(testLabel);
    }

    @Test
    void createLabel_EmptyName_ThrowsException() {
        // Arrange
        testLabel.setName("");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> labelService.createLabel(testLabel));
    }
}
