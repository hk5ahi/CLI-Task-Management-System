import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Task {


    private String title;
    private String description;
    public LocalDateTime createdAt;
    private User createdBy;
    public LocalTime total_time;
    private User assignee;
    public Comment comments;

}
