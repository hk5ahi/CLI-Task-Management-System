package server.service;

import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.dto.*;

import java.util.List;

public interface TaskService {

    String assignTask(String title,String fullname,Manager manager);

    List<Task> getallTasks();
    List<TaskbyEmployeeDTO> viewAllTasksbyManager(Manager manager);
    List<TaskbyEmployeeDTO> viewAllTasksbySupervisor();

    List<TaskbyStatusDTO> viewTasksByStatus(Employee employee);

    List<TaskbyStatusDTO> viewallTasksByStatus();

    List<TaskbyEmployeeandStatusDTO> viewTasksByUser(String userRole);

    String changeTaskStatus(String task, Task.Status status, User person);

    List<TaskInfoDTO> viewAllTasksByStatusCreatedBySingleManager(Manager activeManager);

    Task getTaskByTitle(String title);

    String archiveTask(String title);

    List<ViewAssignTask> viewAssignedTasks(Employee employee);

    List<TaskbyEmployeeandStatusDTO> viewAllTasksByEmployeeAndStatusCreatedBySingleManager(Manager activeManager);

    String createTask(Manager activeManager, String title, String description, double total_time);

}
