package com.flowboard.label.serviceimpl;

import com.flowboard.label.dto.ChecklistDto;
import com.flowboard.label.dto.ChecklistItemDto;
import com.flowboard.label.dto.LabelDto;
import com.flowboard.label.entity.*;
import com.flowboard.label.exception.BadRequestException;
import com.flowboard.label.exception.ResourceNotFoundException;
import com.flowboard.label.mapper.LabelMapper;
import com.flowboard.label.repository.*;
import com.flowboard.label.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final CardLabelRepository cardLabelRepository;
    private final ChecklistRepository checklistRepository;
    private final ChecklistItemRepository checklistItemRepository;

    private Label findLabelById(int labelId) {
        return labelRepository.findByLabelId(labelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Label not found with id: " + labelId));
    }

    @Override
    public LabelDto createLabel(LabelDto labelDto) {
        Label label = LabelMapper.mapToEntity(labelDto);
        if (label.getName() == null || label.getName().trim().isEmpty()) {
            throw new BadRequestException("Label name cannot be empty");
        }
        return LabelMapper.mapToDto(labelRepository.save(label));
    }

    @Override
    public List<LabelDto> getLabelsByBoard(int boardId) {
        return labelRepository.findByBoardId(boardId).stream()
                .map(LabelMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LabelDto getLabelById(int labelId) {
        return LabelMapper.mapToDto(findLabelById(labelId));
    }

    @Override
    public LabelDto updateLabel(int labelId, LabelDto updatedDto) {
        Label existing = findLabelById(labelId);
        if (updatedDto.getName() != null) existing.setName(updatedDto.getName());
        if (updatedDto.getColor() != null) existing.setColor(updatedDto.getColor());
        return LabelMapper.mapToDto(labelRepository.save(existing));
    }

    @Override
    public void deleteLabel(int labelId) {
        findLabelById(labelId);
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
    public List<LabelDto> getLabelsForCard(int cardId) {
        return cardLabelRepository.findByCardId(cardId).stream()
                .map(cl -> LabelMapper.mapToDto(findLabelById(cl.getLabelId())))
                .toList();
    }

    @Override
    public ChecklistDto createChecklist(ChecklistDto checklistDto) {
        Checklist checklist = LabelMapper.mapToEntity(checklistDto);
        if (checklist.getTitle() == null || checklist.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Checklist title cannot be empty");
        }
        long count = checklistRepository.findByCardId(checklist.getCardId()).size();
        checklist.setPosition((int) count);
        return LabelMapper.mapToDto(checklistRepository.save(checklist));
    }

    @Override
    public List<ChecklistDto> getChecklistsByCard(int cardId) {
        return checklistRepository.findByCardId(cardId).stream()
                .map(LabelMapper::mapToDto)
                .collect(Collectors.toList());
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
    public ChecklistItemDto addItem(ChecklistItemDto itemDto) {
        ChecklistItem item = LabelMapper.mapToEntity(itemDto);
        if (item.getText() == null || item.getText().trim().isEmpty()) {
            throw new BadRequestException("Checklist item text cannot be empty");
        }
        item.setIsCompleted(false);
        return LabelMapper.mapToDto(checklistItemRepository.save(item));
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