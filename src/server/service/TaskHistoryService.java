package server.service;

package server.service;

import server.domain.TaskHistory;

import java.util.List;

public interface TaskHistoryService {
    List<TaskHistory> getAllTaskHistories();

    TaskHistory createTaskHistory(String oldStatus, String newStatus, String movedBy);
}
