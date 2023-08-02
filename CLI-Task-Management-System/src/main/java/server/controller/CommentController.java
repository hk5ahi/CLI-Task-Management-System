package server.controller;

import server.service.CommentService;

public class CommentController {

    private final CommentService commentService;


    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
}
