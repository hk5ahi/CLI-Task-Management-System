package server.service;


import server.domain.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAllComments();

    Comment createComment(String createdAt, String createdBy, String body);
}
