package server.service;

import org.springframework.http.ResponseEntity;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.dto.*;

import java.util.List;

public interface TaskService {

    ResponseEntity<String> assignTask(String title,String fullname,Manager manager);

    List<Task> getallTasks();
    List<TaskDTO> viewAllTasksbyUser(User person);
    List<TaskDTO> viewAllTasksbyUser();

    List<TaskDTO> viewTasksByStatus(Employee employee);

    List<TaskDTO> viewAllTasksByStatus();

    List<TaskDTO> viewTasksByUser(String userRole);

    ResponseEntity<String> changeTaskStatus(String task, Task.Status status, User person);

    List<TaskDTO> viewAllTasksByStatusCreatedBySingleManager(Manager activeManager);

    Task getTaskByTitle(String title);

    ResponseEntity<String > archiveTask(String title);

    List<TaskDTO> viewAssignedTasks(Employee employee);

    List<TaskDTO> viewAllTasksByEmployeeAndStatusCreatedBySingleManager(Manager activeManager);

    ResponseEntity<String> createTask(Manager activeManager, String title, String description, double total_time);

}
