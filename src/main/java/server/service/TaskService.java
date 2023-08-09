package server.service;

import org.springframework.http.ResponseEntity;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.dto.*;

import java.util.List;

public interface TaskService {
    
    //don't return response entity

    ResponseEntity<String> assignTask(String title,String fullName,Manager manager);

    List<Task> getAllTasks();
    List<TaskDTO> viewAllTasksCreatedByManager(Manager manager,String employeeName);
    List<TaskDTO> viewAllTasksByUser(String employeeName);

    List<TaskDTO> viewTasksByStatus(Employee employee);

    List<TaskDTO> viewAllTasksByStatus();

    List<TaskDTO> viewTasksByUser(String userRole);

    ResponseEntity<String> changeTaskStatus(String task, Task.Status status, User person);

    List<TaskDTO> viewAllTasksCreatedByManager(Manager activeManager,Task.Status status);



    ResponseEntity<String > archiveTask(String title);

    List<TaskDTO> viewAssignedTasks(Employee employee);

    List<TaskDTO> viewAllTasksCreatedByManager(Manager activeManager,Task.Status status,String employeeName);

    ResponseEntity<String> createTask(Manager activeManager, String title, String description, double total_time);

}
