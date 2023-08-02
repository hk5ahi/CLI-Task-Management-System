package server.dto;

public class TaskbyEmployeeDTO{
    private String title;
    private String description;
    private String assignee;


    public TaskbyEmployeeDTO(String title, String description, String assignee) {
        this.title = title;
        this.description = description;
        this.assignee = assignee;

    }

    // Getters and setters (or lombok annotations) for each field

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


}
