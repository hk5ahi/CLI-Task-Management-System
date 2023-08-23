package server.dto;

import server.domain.Task;

import java.time.Instant;

public class TaskDTO {

    public TaskDTO() {

            this.taskStatus = null;
            this.title = null;
            this.description = null;
            this.createdAt = null;
            this.createdBy = null;
            this.total_time = 0.0;
            this.assignee = null;

        }


    public Task.Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Task.Status taskStatus) {
        this.taskStatus = taskStatus;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public double getTotal_time() {
        return total_time;
    }

    public void setTotal_time(double total_time) {
        this.total_time = total_time;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    private Task.Status taskStatus;
    private String title;

    private String description;
    private Instant createdAt;
    private String createdBy;
    private double total_time;

    private String assignee;
    
    private boolean isArchived;

    public boolean getArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
