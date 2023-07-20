package server.dao.implementation;

import server.dao.TaskDao;
import server.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDaoImpl implements TaskDao {

    private List<Task> tasks = new ArrayList<>();

    @Override
    public List<Task> getAllTasks() {
        return tasks;
    }

    @Override
    public Task getTaskByTitle(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equals(title)) {
                return task;
            }
        }
        return null;
    }

    @Override
    public Task createTask(Task task) {
        tasks.add(task);
        return task;
    }

    @Override
    public boolean updateTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTitle().equals(task.getTitle())) {
                tasks.set(i, task);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteTask(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equals(title)) {
                tasks.remove(task);
                return true;
            }
        }
        return false;
    }
}
