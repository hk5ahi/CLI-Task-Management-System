package server.service.Implementation;

import server.dao.implementation.CommentDaoImpl;
import server.domain.Comment;
import server.domain.Task;
import server.domain.User;
import server.service.CommentService;

import java.time.LocalDateTime;

public class CommentServiceImpl implements CommentService {
    private CommentDaoImpl commentDao = CommentDaoImpl.getInstance();

    @Override
    public void addComments(String message, User person, Task task) {
        Comment comment = new Comment();
        comment.setBody(message);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setCreatedBy(person);
        comment.addTaskForComment(task);

        commentDao.addComment(comment);
        System.out.printf("The comment has been added successfully by %s.\n", person.getFirstName() + " " + person.getLastName());
    }


}
