package server.dao;

import server.domain.Task;
import server.domain.TaskHistory;

public interface TaskHistoryDao {
    TaskHistory getTaskHistory(Task task) ;

    void setTaskHistory(TaskHistory taskHistory, Task task) ;


}
