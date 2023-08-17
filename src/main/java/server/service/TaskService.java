package server.service;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.dto.*;
import java.util.List;


public interface TaskService {



    List<TaskDTO> getAllTasksCreatedByManager(Manager manager, String employeeName);
    List<TaskDTO> getAllTasksByUser(QueryParameterDTO queryParameterDTO);

    List<TaskDTO> getTasksByStatus(Employee employee);

    List<TaskDTO> getAllTasksByStatus(QueryParameterDTO queryParameterDTO);

    List<TaskDTO> getTasksByUser(User.UserRole userRole,QueryParameterDTO queryParameterDTO);


    List<TaskDTO> getAllTasksCreatedByManager(Manager activeManager,Task.Status status);

    void createTaskByController(TaskDTO task,String header);

    List<TaskDTO> getTasksByController( QueryParameterDTO queryParameterDTO,String header);

    List<TaskDTO> getAssignedTasks(Employee employee);

    List<TaskDTO> getAllTasksCreatedByManager(Manager activeManager,Task.Status status,String employeeName);

    void createTask(Manager activeManager, TaskDTO taskDTO);

    void updateTasksByController(String authorizationHeader,TaskDTO taskDTO);

}
