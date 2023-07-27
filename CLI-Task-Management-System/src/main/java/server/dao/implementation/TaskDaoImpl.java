package server.dao.implementation;

import server.dao.TaskDao;
import server.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDaoImpl implements TaskDao {


    private static TaskDaoImpl instance;  // Singleton instance
    private List<Task> Tasks = new ArrayList<>();

    // Private constructor to prevent external instantiation
    private TaskDaoImpl() {
    }

    // Method to get the Singleton instance
    public static TaskDaoImpl getInstance() {
        if (instance == null) {
            instance = new TaskDaoImpl();
        }
        return instance;
    }


    @Override
    public void addTask(Task task) {
        this.Tasks.add(task);
    }


    @Override
    public List<Task> getAllTasksbyEmployee() {
        return this.Tasks;
    }


}
