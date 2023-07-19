import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Task extends Comment {


    enum Status {
        CREATED,
        IN_PROGRESS,
        COMPLETED,
        IN_REVIEW
    }

    public String taskStatus;
    public String title;
    public String description;
    public LocalDateTime createdAt;
    public String createdBy;
    public int total_time;
    public String assignee;
    public List<Comment> comments;
    public boolean assigned;

    public Task() {

    }

    Task(String title, String description, int total_time) {
        taskStatus = String.valueOf(Status.CREATED);
        assigned = false;
        this.title = title;
        this.description = description;
        this.total_time = total_time;

    }


}
