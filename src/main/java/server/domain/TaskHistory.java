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

    public Long getTaskHistoryId() {
        return taskHistoryId;
    }

    public void setTaskHistoryId(Long taskHistoryId) {
        this.taskHistoryId = taskHistoryId;
    }
    @Id
    @Column(name = "taskHistory_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskHistoryId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "title")
    private String title;

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
