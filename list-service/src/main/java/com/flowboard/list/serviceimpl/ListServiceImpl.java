package com.flowboard.list.serviceimpl;

import com.flowboard.list.entity.TaskList;
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
        // Set position at end of board
        Integer maxPosition = listRepository
                .findMaxPositionByBoardId(taskList.getBoardId());
        taskList.setPosition(
                maxPosition != null ? maxPosition + 1 : 0);
        taskList.setIsArchived(false);
        return listRepository.save(taskList);
    }

    @Override
    public TaskList getListById(int listId) {
        return listRepository.findByListId(listId)
                .orElseThrow(() ->
                        new RuntimeException("List not found"));
    }

    @Override
    public List<TaskList> getListsByBoard(int boardId) {
        return listRepository
                .findByBoardIdOrderByPosition(boardId);
    }

    @Override
    public List<TaskList> getArchivedLists(int boardId) {
        return listRepository
                .findByBoardIdAndIsArchived(boardId, true);
    }

    @Override
    public TaskList updateList(int listId, TaskList updated) {
        TaskList existing = getListById(listId);
        if (updated.getName() != null) {
            existing.setName(updated.getName());
        }
        if (updated.getColor() != null) {
            existing.setColor(updated.getColor());
        }
        return listRepository.save(existing);
    }

    @Override
    @Transactional
    public void reorderLists(int boardId,
                             List<Integer> orderedListIds) {
        // Fetch all lists for this board
        List<TaskList> lists = listRepository
                .findByBoardIdOrderByPosition(boardId);

        // Update positions atomically
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
        list.setIsArchived(true);
        listRepository.save(list);
    }

    @Override
    public void unarchiveList(int listId) {
        TaskList list = getListById(listId);
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

        // Set position at end of target board
        Integer maxPosition = listRepository
                .findMaxPositionByBoardId(targetBoardId);
        list.setBoardId(targetBoardId);
        list.setPosition(
                maxPosition != null ? maxPosition + 1 : 0);
        return listRepository.save(list);
    }
}