package server.dto;

public class TaskbyEmployeeDTO{
    private String title;
    private String description;
    private String assignee;

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    private String CreatedBy;

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    private String CreatedAt;


    public TaskbyEmployeeDTO(String title, String description, String assignee,String createdAt,String createdBy) {
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.CreatedAt=createdAt;
        this.CreatedBy=createdBy;

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignee() {
        return assignee;
    }


}
