package com.flowboard.label.serviceimpl;

import com.flowboard.label.entity.*;
import com.flowboard.label.exception.BadRequestException;
import com.flowboard.label.exception.ResourceNotFoundException;
import com.flowboard.label.repository.*;
import com.flowboard.label.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired private LabelRepository labelRepository;
    @Autowired private CardLabelRepository cardLabelRepository;
    @Autowired private ChecklistRepository checklistRepository;
    @Autowired private ChecklistItemRepository checklistItemRepository;

    @Override
    public Label createLabel(Label label) {
        if (label.getName() == null || label.getName().trim().isEmpty()) {
            throw new BadRequestException("Label name cannot be empty");
        }
        return labelRepository.save(label);
    }

    @Override
    public List<Label> getLabelsByBoard(int boardId) {
        return labelRepository.findByBoardId(boardId);
    }

    @Override
    public Label getLabelById(int labelId) {
        return labelRepository.findByLabelId(labelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Label not found with id: " + labelId));
    }

    @Override
    public Label updateLabel(int labelId, Label updated) {
        Label existing = getLabelById(labelId);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getColor() != null) existing.setColor(updated.getColor());
        return labelRepository.save(existing);
    }

    @Override
    public void deleteLabel(int labelId) {
        getLabelById(labelId);
        labelRepository.deleteById(labelId);
    }

    @Override
    public void addLabelToCard(int cardId, int labelId) {
        if (cardLabelRepository.existsByCardIdAndLabelId(cardId, labelId)) {
            throw new BadRequestException("Label " + labelId + " is already added to card " + cardId);
        }
        CardLabel cardLabel = new CardLabel();
        cardLabel.setCardId(cardId);
        cardLabel.setLabelId(labelId);
        cardLabelRepository.save(cardLabel);
    }

    @Override
    @Transactional
    public void removeLabelFromCard(int cardId, int labelId) {
        cardLabelRepository.deleteByCardIdAndLabelId(cardId, labelId);
    }

    @Override
    public List<Label> getLabelsForCard(int cardId) {
        return cardLabelRepository.findByCardId(cardId).stream()
                .map(cl -> getLabelById(cl.getLabelId()))
                .toList();
    }

    @Override
    public Checklist createChecklist(Checklist checklist) {
        if (checklist.getTitle() == null || checklist.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Checklist title cannot be empty");
        }
        long count = checklistRepository.findByCardId(checklist.getCardId()).size();
        checklist.setPosition((int) count);
        return checklistRepository.save(checklist);
    }

    @Override
    public List<Checklist> getChecklistsByCard(int cardId) {
        return checklistRepository.findByCardId(cardId);
    }

    @Override
    @Transactional
    public void deleteChecklist(int checklistId) {
        if (!checklistRepository.existsById(checklistId)) {
            throw new ResourceNotFoundException("Checklist not found with id: " + checklistId);
        }
        checklistRepository.deleteById(checklistId);
    }

    @Override
    public ChecklistItem addItem(ChecklistItem item) {
        if (item.getText() == null || item.getText().trim().isEmpty()) {
            throw new BadRequestException("Checklist item text cannot be empty");
        }
        item.setIsCompleted(false);
        return checklistItemRepository.save(item);
    }

    @Override
    public void toggleItem(int itemId) {
        ChecklistItem item = checklistItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Checklist item not found with id: " + itemId));
        item.setIsCompleted(!item.getIsCompleted());
        checklistItemRepository.save(item);
    }

    @Override
    public int getChecklistProgress(int cardId) {
        List<Checklist> checklists = checklistRepository.findByCardId(cardId);
        if (checklists.isEmpty()) return 0;
        long total = 0, completed = 0;
        for (Checklist checklist : checklists) {
            total += checklistItemRepository.countByChecklistId(checklist.getChecklistId());
            completed += checklistItemRepository.countByChecklistIdAndIsCompleted(checklist.getChecklistId(), true);
        }
        if (total == 0) return 0;
        return (int) ((completed * 100) / total);
    }
}