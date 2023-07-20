package server.service;

import server.domain.Supervisor;

import java.util.List;

public interface SupervisorService {

    static Supervisor getInstance() {
        if (instance == null) {
            // Create the instance if it's not already created
            instance = new Supervisor();
        }
        return instance;
    }

    Supervisor verifyCredentials(String username, String password);

    List<Task> viewAllTasks();

    List<Task> viewTasksByStatus(Task.Status status);

    List<Task> viewTasksByEmployee(String employeeName);

    List<Task> viewTasksByManager(String managerName);

    void archiveTask(String title);

    void addComments(String title, String message);

    void viewTaskHistory(String title);

    List<Employee> viewAllEmployees();

    List<Manager> viewAllManagers();

    void createEmployee(String firstname, String lastname, String username, String password);

    void createManager(String firstname, String lastname, String username, String password);

}

