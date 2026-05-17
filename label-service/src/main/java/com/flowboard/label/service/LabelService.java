package com.flowboard.label.service;

import com.flowboard.label.dto.ChecklistDto;
import com.flowboard.label.dto.ChecklistItemDto;
import com.flowboard.label.dto.LabelDto;

import java.util.List;

public interface LabelService {

    // Label operations
    LabelDto createLabel(LabelDto labelDto);
    List<LabelDto> getLabelsByBoard(int boardId);
    LabelDto getLabelById(int labelId);
    LabelDto updateLabel(int labelId, LabelDto labelDto);
    void deleteLabel(int labelId);

    // Card-Label operations
    void addLabelToCard(int cardId, int labelId);
    void removeLabelFromCard(int cardId, int labelId);
    List<LabelDto> getLabelsForCard(int cardId);

    // Checklist operations
    ChecklistDto createChecklist(ChecklistDto checklistDto);
    List<ChecklistDto> getChecklistsByCard(int cardId);
    void deleteChecklist(int checklistId);

    // ChecklistItem operations
    ChecklistItemDto addItem(ChecklistItemDto itemDto);
    void toggleItem(int itemId);
    int getChecklistProgress(int cardId);
}