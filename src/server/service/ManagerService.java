package server.service;

import server.domain.Manager;
import server.domain.Task;

public interface ManagerService {

    Manager findManager(String username, String password);

    void createTask(Manager activeManager, String title, String description, int total_time);

    void assignTask();

    Task getTaskByTitle(String title);

    String getEmployeeByName(String name);

    void inReviewToCompleted(Manager activeManager);

    void addComments(String message, Manager activeManager);

    void viewAllTasks(Manager activeManager);

    void viewAllTasksByEmployee(Manager activeManager);

    void viewAllTasksByStatus(Manager activeManager);

    void viewAllTasksByEmployeeAndStatus(Manager activeManager);

}
