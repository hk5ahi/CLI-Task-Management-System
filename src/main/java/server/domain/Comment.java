package server.domain;

import java.time.LocalDateTime;

public class Comment {

    private LocalDateTime createdAt;
    private User createdBy;
    private String body;

    private Task tasks;


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

    public Task getTasks() {
        return tasks;
    }

    
    public void addTaskForComment(Task task) {
        this.tasks = task;
    }

}
