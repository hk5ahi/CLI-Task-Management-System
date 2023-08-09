package server.dao;

import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;

import java.util.List;
import java.util.Optional;

public interface TaskDao {

    List<Task> getAll();

    void addTask(Task allTasks);

    List<Task> getAllTasksByManager(Manager manager, Employee employee);
    List<Task> getAllTasksByManager(Manager manager, Employee employee,Task.Status status);

    List<Task> getAllTasksByEmployee(Employee employee);

    List<Task> getAllTasksByUserRole(User.UserRole userRole);
    List<Task> getTasksByStatus(Manager manager,Task.Status status);

    boolean isTaskExist(Task task);

    Optional<Task> getTaskByTitle(String title);



}
