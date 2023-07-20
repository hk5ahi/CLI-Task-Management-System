package server.domain;

import java.util.ArrayList;
import java.util.List;

public class Task {


    public enum Status {
        CREATED,
        IN_PROGRESS,
        COMPLETED,
        IN_REVIEW
    }

    private String taskStatus;
    private taskHistory history;
    private String title;
    private String description;
    private String createdAt;
    private String createdBy;
    private static int total_time;
    private String assignee;
    private List<Comment> comments = new ArrayList<>();
    private boolean assigned;

    public Task(String title, String description, int total_time) {
        taskStatus = String.valueOf(Status.CREATED);
        assigned = false;
        this.title = title;
        this.description = description;
        Task.total_time = total_time;
    }

    // Getters and Setters for the properties
    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public taskHistory getHistory() {
        return history;
    }

    public void setHistory(taskHistory history) {
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public static int getTotal_time() {
        return total_time;
    }

    public static void setTotal_time(int total_time) {
        Task.total_time = total_time;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
