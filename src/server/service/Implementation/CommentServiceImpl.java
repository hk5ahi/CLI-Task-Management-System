package server.service.Implementation;


import server.domain.Comment;
import server.service.CommentService;

import java.util.ArrayList;
import java.util.List;

public class CommentServiceImpl implements CommentService {

    private List<Comment> comments = new ArrayList<>();

    @Override
    public List<Comment> getAllComments() {
        return comments;
    }

    @Override
    public Comment createComment(String createdAt, String createdBy, String body) {
        Comment comment = new Comment(createdAt, createdBy, body);
        comments.add(comment);
        return comment;
    }
}
