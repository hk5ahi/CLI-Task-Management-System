package server.dto;

public class TaskbyEmployeeandStatusDTO {
    private String title;
    private String description;
    private String assignee;
    private String taskStatus;


    public TaskbyEmployeeandStatusDTO(String title, String description, String assignee, String taskStatus) {
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.taskStatus = taskStatus;

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
