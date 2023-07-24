package server.dao;

import server.domain.Comment;
import server.domain.Manager;
import server.domain.Task;

import java.util.List;

public interface TaskDao {

    List<Comment> getComments(Task task);

    void addComment(Comment comment, Task task);

    void createTask(Manager activeManager, String title, String description, int total_time);

}
