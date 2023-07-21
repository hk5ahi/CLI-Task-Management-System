package server.service;


import server.domain.User;

public interface CommentService {

    void addComments(String message, User person);

}
