package com.flowboard.list.service;

import com.flowboard.list.entity.TaskList;
import java.util.List;

public interface ListService {

    TaskList createList(TaskList taskList);

    TaskList getListById(int listId);

    List<TaskList> getListsByBoard(int boardId);

    List<TaskList> getArchivedLists(int boardId);

    TaskList updateList(int listId, TaskList taskList);

    void reorderLists(int boardId,
                      List<Integer> orderedListIds);

    void archiveList(int listId);

    void unarchiveList(int listId);

    void deleteList(int listId);

    TaskList moveList(int listId, int targetBoardId);
}