package server.dao.implementation;

import org.springframework.stereotype.Repository;
import server.dao.TaskDao;
import server.dao.TaskHistoryDao;
import server.domain.Task;
import server.domain.TaskHistory;

@Repository
public class TaskHistoryDaoImpl implements TaskHistoryDao {


    private final TaskDao taskDao;
    private TaskHistory taskHistory;
    public TaskHistoryDaoImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }
    @Override
    public TaskHistory getTaskHistory(Task task) {

        for(Task giventask:taskDao.getAll())
        {
            if(giventask.getTitle().equals(task.getTitle()))
            {
                return taskHistory;

            }
        }
        return null;
    }
@Override
    public void setTaskHistory(TaskHistory taskHistory, Task task) {
        for(Task giventask:taskDao.getAll())
        {
            if(giventask.getTitle().equals(task.getTitle()))
            {
                giventask.setHistory(taskHistory);

            }
        }
    }





}
