package server.service;
import server.domain.User;
import server.dto.CommentDTO;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    void addComment(String message, User person, String task);

    Optional<List<CommentDTO>> getComment(String task);

    void addCommentByUser(CommentDTO commentDTO,String header);

    Optional<List<CommentDTO>> getCommentByController(String title, String header);
}
