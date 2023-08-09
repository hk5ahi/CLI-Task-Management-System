package server.domain;

import java.time.Instant;

public class Comment {

    private Instant createdAt;
    private User createdBy;
    private String body;

    private Task tasks;


    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
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
