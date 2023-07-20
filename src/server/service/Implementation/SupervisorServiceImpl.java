package server.service.Implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class SupervisorServiceImpl implements SupervisorService {

    private Supervisor supervisor = Supervisor.getInstance();

    @Override
    public Supervisor verifyCredentials(String username, String password) {
        return Supervisor.verifyCredentials(username, password);
    }

    @Override
    public List<Task> viewAllTasks() {
        return Employee.allTasks;
    }

    @Override
    public List<Task> viewTasksByStatus(Task.Status status) {
        // Implementation for viewing tasks by status
        // ...
        return null;
    }

    @Override
    public List<Task> viewTasksByEmployee(String employeeName) {
        // Implementation for viewing tasks categorized employee-wise
        // ...
        return null;
    }

    @Override
    public List<Task> viewTasksByManager(String managerName) {
        // Implementation for viewing tasks categorized manager-wise
        // ...
        return null;
    }

    @Override
    public void archiveTask(String title) {
        // Implementation for archiving a task
        // ...
    }

    @Override
    public void addComments(String title, String message) {
        // Implementation for adding comments to a task
        // ...
    }

    @Override
    public void viewTaskHistory(String title) {
        // Implementation for viewing task history
        // ...
    }

    @Override
    public List<Employee> viewAllEmployees() {
        return Employee.employees;
    }

    @Override
    public List<Manager> viewAllManagers() {
        return Manager.managers;
    }

    @Override
    public void createEmployee(String firstname, String lastname, String username, String password) {
        Supervisor.createEmployee(firstname, lastname, username, password);
    }

    @Override
    public void createManager(String firstname, String lastname, String username, String password) {
        Supervisor.createManager(firstname, lastname, username, password);
    }
}
