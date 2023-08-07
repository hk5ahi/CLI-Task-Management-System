package server.dao.implementation;

import org.springframework.stereotype.Repository;
import server.dao.TaskDao;
import server.domain.Task;

import java.util.ArrayList;
import java.util.List;
@Repository
public class TaskDaoImpl implements TaskDao {

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        this.tasks.add(task);
    }


    @Override
    public List<Task> getAll() {
        return this.tasks;
    }


}
