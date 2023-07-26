package server.domain;

import java.time.Instant;

public class Task {

    public enum Status {
        CREATED,
        IN_PROGRESS,
        COMPLETED,
        IN_REVIEW
    }

    private String taskStatus;
    private TaskHistory history;
    private String title;
    private String description;
    private String createdAt;
    private User createdBy;
    private double total_time;
    private User assignee;
    private boolean assigned;
    private Instant startTime;

    public Task(String title, String description, int total_time) {
        this.setTaskStatus(String.valueOf(Status.CREATED));
        this.setAssigned(false);
        this.setTitle(title);
        this.setDescription(description);
        this.setTotal_time(total_time);
    }

    // Getters and Setters for the properties
    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskHistory getHistory() {
        return history;
    }

    public void setHistory(TaskHistory history) {
        this.history = history;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public double getTotal_time() {
        return total_time;
    }

    public void setTotal_time(double total_time) {
        this.total_time = total_time;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }
}
