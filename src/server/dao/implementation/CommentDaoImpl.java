package server.dao.implementation;

import server.dao.CommentDao;
import server.domain.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentDaoImpl implements CommentDao {

    private List<Comment> comments = new ArrayList<>();

    @Override
    public List<Comment> getAllComments() {
        return comments;
    }

    @Override
    public Comment createComment(Comment comment) {
        comments.add(comment);
        return comment;
    }
}
