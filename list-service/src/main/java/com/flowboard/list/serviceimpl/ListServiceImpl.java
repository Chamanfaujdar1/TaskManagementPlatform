package com.flowboard.list.serviceimpl;

import com.flowboard.list.entity.TaskList;
import com.flowboard.list.exception.BadRequestException;
import com.flowboard.list.exception.ResourceNotFoundException;
import com.flowboard.list.repository.ListRepository;
import com.flowboard.list.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListServiceImpl implements ListService {

    @Autowired
    private ListRepository listRepository;

    @Override
    public TaskList createList(TaskList taskList) {
        if (taskList.getName() == null || taskList.getName().trim().isEmpty()) {
            throw new BadRequestException("List name cannot be empty");
        }
        Integer maxPosition = listRepository.findMaxPositionByBoardId(taskList.getBoardId());
        taskList.setPosition(maxPosition != null ? maxPosition + 1 : 0);
        taskList.setIsArchived(false);
        return listRepository.save(taskList);
    }

    @Override
    public TaskList getListById(int listId) {
        return listRepository.findByListId(listId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("List not found with id: " + listId));
    }

    @Override
    public List<TaskList> getListsByBoard(int boardId) {
        return listRepository.findByBoardIdOrderByPosition(boardId);
    }

    @Override
    public List<TaskList> getArchivedLists(int boardId) {
        return listRepository.findByBoardIdAndIsArchived(boardId, true);
    }

    @Override
    public TaskList updateList(int listId, TaskList updated) {
        TaskList existing = getListById(listId);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getColor() != null) existing.setColor(updated.getColor());
        return listRepository.save(existing);
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
        TaskList list = getListById(listId);
        if (list.getIsArchived()) {
            throw new BadRequestException("List is already archived");
        }
        list.setIsArchived(true);
        listRepository.save(list);
    }

    @Override
    public void unarchiveList(int listId) {
        TaskList list = getListById(listId);
        if (!list.getIsArchived()) {
            throw new BadRequestException("List is not archived");
        }
        list.setIsArchived(false);
        listRepository.save(list);
    }

    @Override
    @Transactional
    public void deleteList(int listId) {
        getListById(listId);
        listRepository.deleteByListId(listId);
    }

    @Override
    public TaskList moveList(int listId, int targetBoardId) {
        TaskList list = getListById(listId);
        Integer maxPosition = listRepository.findMaxPositionByBoardId(targetBoardId);
        list.setBoardId(targetBoardId);
        list.setPosition(maxPosition != null ? maxPosition + 1 : 0);
        return listRepository.save(list);
    }
}