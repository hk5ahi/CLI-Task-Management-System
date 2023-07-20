package server.dao.implementation;

import server.dao.TaskHistoryDao;
import server.domain.TaskHistory;

import java.util.ArrayList;
import java.util.List;

public class TaskHistoryDaoImpl implements TaskHistoryDao {

    private List<TaskHistory> taskHistories = new ArrayList<>();

    @Override
    public List<TaskHistory> getAllTaskHistories() {
        return taskHistories;
    }

    @Override
    public TaskHistory createTaskHistory(TaskHistory taskHistory) {
        taskHistories.add(taskHistory);
        return taskHistory;
    }
}
