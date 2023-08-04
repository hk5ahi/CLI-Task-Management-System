package server.dao.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.dao.CommentDao;
import server.domain.Comment;
import server.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class CommentDaoImpl implements CommentDao {

    private static CommentDaoImpl instance;  // Singleton instance
    private List<Comment> comments = new ArrayList<>();

    // Private constructor to prevent external instantiation
    private CommentDaoImpl() {
    }

    // Method to get the Singleton instance
    public static CommentDaoImpl getInstance() {
        if (instance == null) {
            instance = new CommentDaoImpl();
        }
        return instance;
    }

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
