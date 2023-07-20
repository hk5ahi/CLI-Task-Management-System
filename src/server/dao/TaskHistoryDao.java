package server.dao;

import server.domain.TaskHistory;

import java.util.List;

public interface TaskHistoryDao {
    List<TaskHistory> getAllTaskHistories();

    TaskHistory createTaskHistory(TaskHistory taskHistory);
}
