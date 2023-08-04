package server.dto;

import server.domain.Task;
import server.domain.User;

public class TaskStatusDTO {

    private String title;
    private Task.Status status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Task.Status getStatus() {
        return status;
    }

    public void setStatus(Task.Status status) {
        this.status = status;
    }




}
