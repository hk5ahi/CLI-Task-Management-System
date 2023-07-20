import java.util.ArrayList;
import java.util.List;

public class Task {


    enum Status {
        CREATED,
        IN_PROGRESS,
        COMPLETED,
        IN_REVIEW
    }

    public String taskStatus;

    public taskHistory history;
    public String title;
    public String description;
    public String createdAt;
    public String createdBy;
    public static int total_time;
    public String assignee;
    public List<Comment> comments = new ArrayList<>();
    public boolean assigned;


    Task(String title, String description, int total_time) {
        taskStatus = String.valueOf(Status.CREATED);
        assigned = false;
        this.title = title;
        this.description = description;
        this.total_time = total_time;

    }


}
