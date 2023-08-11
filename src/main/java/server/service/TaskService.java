package server.service;

import org.springframework.http.ResponseEntity;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.dto.*;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    void assignTask(String title,String fullName,Manager manager);

//    Optional<List<Task>> getAllTasks();
    Optional<List<TaskDTO>> getAllTasksCreatedByManager(Manager manager, String employeeName);
    Optional<List<TaskDTO>> getAllTasksByUser(String employeeName);

    Optional<List<TaskDTO>> getTasksByStatus(Employee employee);

    Optional<List<TaskDTO>> getAllTasksByStatus();

    Optional<List<TaskDTO>> getTasksByUser(User.UserRole userRole);

    void changeTaskStatus(String task, Task.Status status, User person);

    Optional<List<TaskDTO>> getAllTasksCreatedByManager(Manager activeManager,Task.Status status);

    void createTaskByController(TaskDTO task,String header);

    Optional<List<TaskDTO>> getTasksByController( boolean status,boolean employeeRole, boolean assigned, User.UserRole userRole,boolean manager,boolean noCriteria,Task.Status task_status,String employeeName,String header);
    void archiveTask(String title);

    Optional<List<TaskDTO>> getAssignedTasks(Employee employee);

    Optional<List<TaskDTO>> getAllTasksCreatedByManager(Manager activeManager,Task.Status status,String employeeName);

    void createTask(Manager activeManager, String title, String description, double total_time);

    void updateTasksByController(boolean status,boolean archive,boolean assign,boolean time, double timeValue,String authorizationHeader,TaskDTO taskDTO);

}
