package server.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Task {

    public enum Status {
        CREATED,
        IN_PROGRESS,
        COMPLETED,
        IN_REVIEW
    }

    private List<TaskHistory> taskHistory=new ArrayList<>();
    private String title;
    private String description;
    private Instant createdAt;
    private User createdBy;
    private double total_time;
    private User assignee;
    private boolean assigned;
    private Instant startTime;

    public Task(String title, String description, double total_time) {
        this.setTaskStatus(Status.CREATED);
        this.setAssigned(false);
        this.setTitle(title);
        this.setDescription(description);
        this.setTotal_time(total_time);
    }

    private Status taskStatus;

    public List<TaskHistory> getHistory(Task task) {
        return task.taskHistory;
    }

    public void setHistory(TaskHistory history) {
        taskHistory.add(history);
    }
    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
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
