package server.domain;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "tasks")
public class Task {

    public Task() {

    }

    public enum Status {
        CREATED,
        IN_PROGRESS,
        COMPLETED,
        IN_REVIEW
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_Id")
    private Long taskId;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status",nullable = false)
    private Status taskStatus;

    @Column(name = "title",nullable = false,length = 25,columnDefinition = "TEXT",unique = true)
    private String title;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at",nullable = false)
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by",nullable = false)
    private User createdBy;

    @Column(name = "total_time",length = 10)
    private double total_time;

    @ManyToOne
    @JoinColumn(name = "assignee")
    private User assignee;

    @Column(name = "assigned",nullable = false)
    private boolean assigned;

    @Column(name = "start_Time")
    private Instant startTime;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private List<TaskHistory> taskHistory = new ArrayList<>();



    public Task(String title, String description, double total_time) {
        this.setTaskStatus(Status.CREATED);
        this.setTitle(title);
        this.setDescription(description);
        this.setTotal_time(total_time);
        this.setArchived(false);
    }

    public List<TaskHistory> getHistory(Task task) {
        return task.taskHistory;
    }

    public void setHistory(TaskHistory history) {
        taskHistory.add(history);
        history.setTask(this);
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

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }
}