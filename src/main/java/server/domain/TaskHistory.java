package server.domain;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
@Table(name = "task_history")
public class TaskHistory {
    @Column(name = "timestamp")
    private Instant timestamp;
    @Enumerated(EnumType.STRING)
    @Column(name = "old_status",nullable = false)
    private Task.Status oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status",nullable = false)
    private Task.Status newStatus;
    @ManyToOne
    @JoinColumn(name = "moved_by")
    private User movedBy;

    @Id
    @Column(name = "taskHistory_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskHistoryId;
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Task.Status getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Task.Status oldStatus) {
        this.oldStatus = oldStatus;
    }

    public Task.Status getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Task.Status newStatus) {
        this.newStatus = newStatus;
    }

    public User getMovedBy() {
        return movedBy;
    }
    public void setMovedBy(User movedBy) {
        this.movedBy = movedBy;
    }


}