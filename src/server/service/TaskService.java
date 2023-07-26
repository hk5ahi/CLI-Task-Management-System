package server.service;

import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;

import java.util.List;

public interface TaskService {

    void assignTask();

    void viewAllTasks();

    void viewTasksByStatus(Employee employee);

    void viewallTasksByStatus();

    void viewTasksByUser(User person);

    void changeTaskStatus(Task task, Task.Status status, User person);

    void viewAllTasksByStatusCreatedBySingleManager(Manager activeManager);

    Task getTaskByTitle(String title);

    void archiveTask();

    void viewAssignedTasks(Employee employee);

    void viewAllTasksByEmployeeAndStatusCreatedBySingleManager(Manager activeManager);

    void createTask(Manager activeManager, String title, String description, int total_time);

    void printTasksByStatus(List<Task> tasks, Task.Status status, Employee employee);

    void printallTasksByStatus(List<Task> tasks, Task.Status status);
}
