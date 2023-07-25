package server.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Comment {

    private LocalDateTime createdAt;
    private User createdBy;
    private String body;

    private List<Task> tasks = new ArrayList<>();


    // Getters and Setters
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    // Setter for the tasks field
    public void addTaskForComment(Task task) {
        this.tasks.add(task);
    }
}
