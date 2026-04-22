package com.flowboard.label.service;

import com.flowboard.label.entity.Checklist;
import com.flowboard.label.entity.ChecklistItem;
import com.flowboard.label.entity.Label;

import java.util.List;

public interface LabelService {

    // Label operations
    Label createLabel(Label label);
    List<Label> getLabelsByBoard(int boardId);
    Label getLabelById(int labelId);
    Label updateLabel(int labelId, Label label);
    void deleteLabel(int labelId);

    // Card-Label operations
    void addLabelToCard(int cardId, int labelId);
    void removeLabelFromCard(int cardId, int labelId);
    List<Label> getLabelsForCard(int cardId);

    // Checklist operations
    Checklist createChecklist(Checklist checklist);
    List<Checklist> getChecklistsByCard(int cardId);
    void deleteChecklist(int checklistId);

    // ChecklistItem operations
    ChecklistItem addItem(ChecklistItem item);
    void toggleItem(int itemId);
    int getChecklistProgress(int cardId);
}