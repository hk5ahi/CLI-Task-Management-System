package server.dto;

import server.domain.Task;


public class TaskHistoryDTO {

    private Task.Status oldStatus;

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

    public String getMovedBy() {
        return movedBy;
    }

    public void setMovedBy(String movedBy) {
        this.movedBy = movedBy;
    }

    public String getMovedAt() {
        return movedAt;
    }

    public void setMovedAt(String movedAt) {
        this.movedAt = movedAt;
    }

    private Task.Status newStatus;

    private String movedBy;
    private String movedAt;
}
