package server.dao.implementation;


import org.springframework.stereotype.Repository;
import server.dao.CommentDao;
import server.domain.Comment;
import server.domain.Task;

import java.util.ArrayList;
import java.util.List;
@Repository
public class CommentDaoImpl implements CommentDao {


    private final List<Comment> comments = new ArrayList<>();

    @Override
    public List<Comment> getComments(Task task) {
        List<Comment> commentsForTask = new ArrayList<>();

            for (Comment comment : comments) {
                Task commentTask = comment.getTasks();
                if (commentTask != null && commentTask.getTitle() != null && commentTask.getTitle().equals(task.getTitle())) {
                    commentsForTask.add(comment);
                }
            }

            return commentsForTask.isEmpty() ? null : commentsForTask;
        }


    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
    }

}
