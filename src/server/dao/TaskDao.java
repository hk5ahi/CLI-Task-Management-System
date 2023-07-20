package server.dao;

import server.domain.Task;

import java.util.List;

public interface TaskDao {
    List<Task> getAllTasks();

    Task getTaskByTitle(String title);

    Task createTask(Task task);

    boolean updateTask(Task task);

    boolean deleteTask(String title);
}
