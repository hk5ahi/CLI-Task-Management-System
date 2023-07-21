package server.service;
import server.domain.Supervisor;
import server.domain.Task;


public interface SupervisorService {


    Supervisor verifyCredentials(String username, String password);

    void viewAllTasks();

    void viewTasksByStatus();

    void viewTasksByEmployee();

    void viewTasksByManager();

    Task getTaskByTitle(String title);

    void archiveTask();

    void addComments(String message);

    void viewTaskHistory();

    void viewSupervisor();

    void viewAllEmployees();

    void viewAllManagers();

    void createEmployee(String firstname, String lastname, String username, String password);

    void createManager(String firstname, String lastname, String username, String password);

}

