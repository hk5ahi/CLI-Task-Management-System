package server.dao;

import server.domain.Comment;

import java.util.List;

public interface CommentDao {
    List<Comment> getAllComments();

    Comment createComment(Comment comment);
}
