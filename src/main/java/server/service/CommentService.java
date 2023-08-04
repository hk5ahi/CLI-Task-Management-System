package server.service;
import server.domain.Comment;
import server.domain.Task;
import server.domain.User;
import server.dto.ViewCommentDTO;

import java.util.List;

public interface CommentService {

    String addComments(String message, User person, String task);

    List<ViewCommentDTO> viewComments(String task);

}
