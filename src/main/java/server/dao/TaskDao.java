package server.dao;

import server.domain.Task;

import java.util.List;

public interface TaskDao {

    List<Task> getAll();

    void addTask(Task allTasks);


}
