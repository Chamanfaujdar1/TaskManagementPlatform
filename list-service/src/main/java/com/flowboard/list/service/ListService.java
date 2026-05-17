package com.flowboard.list.service;

import com.flowboard.list.dto.TaskListDto;
import java.util.List;

public interface ListService {

    TaskListDto createList(TaskListDto taskListDto);

    TaskListDto getListById(int listId);

    List<TaskListDto> getListsByBoard(int boardId);

    List<TaskListDto> getArchivedLists(int boardId);

    TaskListDto updateList(int listId, TaskListDto taskListDto);

    void reorderLists(int boardId,
                      List<Integer> orderedListIds);

    void archiveList(int listId);

    void unarchiveList(int listId);

    void deleteList(int listId);

    TaskListDto moveList(int listId, int targetBoardId);
}