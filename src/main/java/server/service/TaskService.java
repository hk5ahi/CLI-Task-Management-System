package server.service;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.dto.*;
import java.util.List;


public interface TaskService {

    void assignTask(TaskDTO taskDTO,Manager manager);

    List<TaskDTO> getAllTasksCreatedByManager(Manager manager, String employeeName);
    List<TaskDTO> getAllTasksByUser(String employeeName);

    List<TaskDTO> getTasksByStatus(Employee employee);

    List<TaskDTO> getAllTasksByStatus();

    List<TaskDTO> getTasksByUser(User.UserRole userRole);

    void changeTaskStatus(TaskDTO taskDTO, User person);

    List<TaskDTO> getAllTasksCreatedByManager(Manager activeManager,Task.Status status);

    void createTaskByController(TaskDTO task,String header);

    List<TaskDTO> getTasksByController( boolean status,boolean employeeRole, boolean assigned, User.UserRole userRole,boolean manager,boolean noCriteria,Task.Status task_status,String employeeName,String header);
    void archiveTask(TaskDTO taskDTO);

    List<TaskDTO> getAssignedTasks(Employee employee);

    List<TaskDTO> getAllTasksCreatedByManager(Manager activeManager,Task.Status status,String employeeName);

    void createTask(Manager activeManager, TaskDTO taskDTO);

    void updateTasksByController(String authorizationHeader,TaskDTO taskDTO);

}
