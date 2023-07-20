package server.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Manager extends User {

    private List<Task> createdTasks = new ArrayList<>();

    public Manager(String firstname, String lastname, String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstName(firstname);
        this.setLastName(lastname);
        managers.add(this);
    }

    public static Manager findManager(String providedUsername, String providedPassword) {
        for (Manager manager : managers) {
            if (manager.getUsername().equals(providedUsername) && manager.getPassword().equals(providedPassword)) {
                return manager;
            }
        }
        return null;
    }

    public void createTask(String title, String description, int total_time) {
        Task t = new Task(title, description, total_time);
        t.setCreatedBy(getFirstName() + " " + getLastName());
        t.setCreatedAt(LocalDateTime.now().toString());
        Employee.addAllTask(t);
        createdTasks.add(t);
    }

    public List<Task> getCreatedTasks() {
        return createdTasks;
    }

    public void setCreatedTasks(List<Task> createdTasks) {
        this.createdTasks = createdTasks;
    }

    // You can add other getters and setters for remaining properties if needed
}
