package server.service;

import server.domain.Employee;
import server.domain.Task;

public interface EmployeeService {


    void empCheck();

    Employee findEmployee(String providedUsername, String providedPassword);


    void createdToInProgress(Employee employee);

    void inProgressToInReview(Employee employee);

    Task getTaskByTitle(String title);

    void addComments(String message, Employee employee);

    void viewAssignedTasks(Employee employee);

    void viewAllTasks();

    // Other methods if needed
}
