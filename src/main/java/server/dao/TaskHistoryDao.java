package server.dao;

import server.domain.Task;
import server.domain.TaskHistory;

import java.util.List;
import java.util.Optional;

public interface TaskHistoryDao {
    Optional<List<TaskHistory>> getTaskHistory(String title) ;

    void setTaskHistory(TaskHistory taskHistory, String title) ;


}
