package com.flowboard.list.serviceimpl;

import com.flowboard.list.dto.TaskListDto;
import com.flowboard.list.entity.TaskList;
import com.flowboard.list.exception.BadRequestException;
import com.flowboard.list.exception.ResourceNotFoundException;
import com.flowboard.list.mapper.TaskListMapper;
import com.flowboard.list.repository.ListRepository;
import com.flowboard.list.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListServiceImpl implements ListService {

    private final ListRepository listRepository;

    private TaskList findListById(int listId) {
        return listRepository.findByListId(listId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("List not found with id: " + listId));
    }

    @Override
    public TaskListDto createList(TaskListDto taskListDto) {
        TaskList taskList = TaskListMapper.mapToEntity(taskListDto);
        if (taskList.getName() == null || taskList.getName().trim().isEmpty()) {
            throw new BadRequestException("List name cannot be empty");
        }
        Integer maxPosition = listRepository.findMaxPositionByBoardId(taskList.getBoardId());
        taskList.setPosition(maxPosition != null ? maxPosition + 1 : 0);
        taskList.setIsArchived(false);
        return TaskListMapper.mapToDto(listRepository.save(taskList));
    }

    @Override
    public TaskListDto getListById(int listId) {
        return TaskListMapper.mapToDto(findListById(listId));
    }

    @Override
    public List<TaskListDto> getListsByBoard(int boardId) {
        return listRepository.findByBoardIdOrderByPosition(boardId).stream()
                .map(TaskListMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskListDto> getArchivedLists(int boardId) {
        return listRepository.findByBoardIdAndIsArchived(boardId, true).stream()
                .map(TaskListMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskListDto updateList(int listId, TaskListDto updated) {
        TaskList existing = findListById(listId);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getColor() != null) existing.setColor(updated.getColor());
        return TaskListMapper.mapToDto(listRepository.save(existing));
    }

    @Override
    @Transactional
    public void reorderLists(int boardId, List<Integer> orderedListIds) {
        if (orderedListIds == null || orderedListIds.isEmpty()) {
            throw new BadRequestException("Ordered list IDs cannot be empty");
        }
        List<TaskList> lists = listRepository.findByBoardIdOrderByPosition(boardId);
        for (int i = 0; i < orderedListIds.size(); i++) {
            final int newPosition = i;
            final int listId = orderedListIds.get(i);
            lists.stream()
                    .filter(l -> l.getListId().equals(listId))
                    .findFirst()
                    .ifPresent(l -> {
                        l.setPosition(newPosition);
                        listRepository.save(l);
                    });
        }
    }

    @Override
    public void archiveList(int listId) {
        TaskList list = findListById(listId);
        if (list.getIsArchived()) {
            throw new BadRequestException("List is already archived");
        }
        list.setIsArchived(true);
        listRepository.save(list);
    }

    @Override
    public void unarchiveList(int listId) {
        TaskList list = findListById(listId);
        if (!list.getIsArchived()) {
            throw new BadRequestException("List is not archived");
        }
        list.setIsArchived(false);
        listRepository.save(list);
    }

    @Override
    @Transactional
    public void deleteList(int listId) {
        findListById(listId);
        listRepository.deleteByListId(listId);
    }

    @Override
    public TaskListDto moveList(int listId, int targetBoardId) {
        TaskList list = findListById(listId);
        Integer maxPosition = listRepository.findMaxPositionByBoardId(targetBoardId);
        list.setBoardId(targetBoardId);
        list.setPosition(maxPosition != null ? maxPosition + 1 : 0);
        return TaskListMapper.mapToDto(listRepository.save(list));
    }
}