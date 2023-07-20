package server.service.Implementation;

package server.service;

import server.domain.TaskHistory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskHistoryServiceImpl implements TaskHistoryService {

    private List<TaskHistory> taskHistories = new ArrayList<>();

    @Override
    public List<TaskHistory> getAllTaskHistories() {
        return taskHistories;
    }

    @Override
    public TaskHistory createTaskHistory(String oldStatus, String newStatus, String movedBy) {
        TaskHistory history = new TaskHistory(LocalDateTime.now(), oldStatus, newStatus, movedBy);
        taskHistories.add(history);
        return history;
    }
}
