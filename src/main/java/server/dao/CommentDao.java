package server.dao;

import server.domain.Comment;
import server.domain.Task;

import java.util.List;

public interface CommentDao {
    List<Comment> getComments(Task task);

    void addComment(Comment comment);
}
