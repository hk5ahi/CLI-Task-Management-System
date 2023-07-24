package server.service;

import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;

public interface TaskService {


    void assignTask();

    void viewAllTasks();

    void viewTasksByStatus();

    void viewTasksByEmployee();

    void viewTasksByManager();

    void viewAllTasksByStatusCreatedBySingleManager(Manager activeManager);

    Task getTaskByTitle(String title);

    void archiveTask();

    void viewAssignedTasks(Employee employee);

    void viewAllTasksByEmployeeAndStatusCreatedBySingleManager(Manager activeManager);


}
