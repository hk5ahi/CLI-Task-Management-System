package server.dao.implementation;

import server.dao.EmployeeDao;
import server.dao.TaskDao;
import server.domain.Comment;
import server.domain.Manager;
import server.domain.Task;

import java.time.LocalTime;
import java.util.List;

public class TaskDaoImpl implements TaskDao {

    @Override
    public List<Comment> getComments(Task task) {
        return task.getComments(task);
    }

    @Override
    public void addComment(Comment comment, Task task) {
        task.getComments(task).add(comment);
    }

    @Override
    public void createTask(Manager activeManager, String title, String description, int total_time) {
        Task t = new Task(title, description, total_time);
        t.setCreatedBy(activeManager.getFirstName() + " " + activeManager.getLastName());

        t.setCreatedAt(LocalTime.now().toString());
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        employeeDao.addTask(t);
    }
}
