package server.service;

import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.dto.TaskInfoDTO;
import server.dto.TaskbyEmployeeDTO;
import server.dto.TaskbyEmployeeandStatusDTO;
import server.dto.TaskbyStatusDTO;

import java.util.List;

public interface TaskService {

    String assignTask(String title,String fullname);

    List<Task> getallTasks();
    List<TaskbyEmployeeDTO> viewAllTasks(Manager manager);

    List<TaskbyStatusDTO> viewTasksByStatus(Employee employee);

    void viewallTasksByStatus();

    void viewTasksByUser(User person);

    void changeTaskStatus(Task task, Task.Status status, User person);

    List<TaskInfoDTO> viewAllTasksByStatusCreatedBySingleManager(Manager activeManager);

    Task getTaskByTitle(String title);

    void archiveTask();

    List<Task> viewAssignedTasks(Employee employee);

    List<TaskbyEmployeeandStatusDTO> viewAllTasksByEmployeeAndStatusCreatedBySingleManager(Manager activeManager);

    void createTask(Manager activeManager, String title, String description, double total_time);



    void printallTasksByStatus(List<Task> tasks, Task.Status status);
}
