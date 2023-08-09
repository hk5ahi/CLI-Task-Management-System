package server.domain;

import java.time.Instant;

public class TaskHistory {

    private Instant timestamp;
    private Task.Status oldStatus;
    private Task.Status newStatus;
    private User movedBy;


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

    public void add(TaskHistory history) {
    }
}
