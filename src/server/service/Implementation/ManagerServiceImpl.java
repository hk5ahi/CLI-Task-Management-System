import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManagerServiceImpl implements ManagerService {

    private List<Manager> managers = new ArrayList<>();

    @Override
    public Manager findManager(String username, String password) {
        for (Manager manager : managers) {
            if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
                return manager;
            }
        }
        return null;
    }

    @Override
    public boolean addManager(Manager manager) {
        if (!managers.contains(manager)) {
            managers.add(manager);
            return true;
        }
        return false;
    }

    @Override
    public void deleteManager(String username) {
        managers.removeIf(manager -> manager.getUsername().equals(username));
    }

    @Override
    public List<Manager> getAllManagers() {
        return managers;
    }

    @Override
    public void createTask(String title, String description, int total_time) {
        Task t = new Task(title, description, total_time);
        t.setCreatedBy(first_Name + " " + last_Name);
        t.setCreatedAt(LocalDateTime.now().toString());
        Employee.addAllTask(t);
    }

    @Override
    public void assignTask() {
        // Implementation for assigning tasks
        // ...
    }

    @Override
    public void inReviewToCompleted() {
        // Implementation for changing task status from "IN_REVIEW" to "COMPLETED"
        // ...
    }

    @Override
    public void addComments(String message) {
        // Implementation for adding comments to a task
        // ...
    }

    @Override
    public void viewAllTasks() {
        // Implementation for viewing all tasks created by the manager
        // ...
    }

    @Override
    public void viewAllTasksByEmployee() {
        // Implementation for viewing all tasks created by the manager and categorized by employee
        // ...
    }

    @Override
    public void viewAllTasksByStatus() {
        // Implementation for viewing all tasks created by the manager and categorized by status
        // ...
    }

    @Override
    public void viewAllTasksByEmployeeAndStatus() {
        // Implementation for viewing all tasks created by the manager and categorized by employee and status
        // ...
    }
}
