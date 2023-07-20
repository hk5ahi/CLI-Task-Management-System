package server.service;

import java.util.List;

public interface TaskService {

    Task createTask(String title, String description, int total_time);

    List<Task> getAllTasks();

    Task getTaskByTitle(String title);

    void assignTask(Task task, String assignee);

    void changeTaskStatus(Task task, Task.Status status);

    void archiveTask(Task task);

    void addComment(Task task, String message);

    List<Comment> getCommentsForTask(Task task);

    List<TaskHistory> getTaskHistory(Task task);

}
