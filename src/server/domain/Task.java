package server.domain;
import java.time.LocalDateTime;
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
    private TaskHistory history;
    private String title;
    private String description;
    private String createdAt;
    private String createdBy;
    private int total_time;
    private String assignee;
    private List<Comment> comments = new ArrayList<>();
    private boolean assigned;

    private LocalDateTime startTime;

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getTotal_time() {
        return total_time;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public List<Comment> getComments(Task task) {
        return task.comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
