package server.dto;

public class TaskbyEmployeeandStatusDTO {
    private String title;
    private String description;
    private String assignee;
    private String taskStatus;

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    private String CreatedBy;

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    private String CreatedAt;

    public TaskbyEmployeeandStatusDTO()
    {}

    public TaskbyEmployeeandStatusDTO(String title, String description, String assignee, String taskStatus, String createdAt) {
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.taskStatus = taskStatus;

        CreatedAt = createdAt;
    }



    // Example getters (you can generate these using your IDE or Lombok)
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getTaskStatus() {
        return taskStatus;
    }


}
