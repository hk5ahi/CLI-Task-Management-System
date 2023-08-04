package server.service;
import org.springframework.http.ResponseEntity;
import server.domain.User;
import server.dto.CommentDTO;

import java.util.List;

public interface CommentService {

    ResponseEntity<String> addComments(String message, User person, String task);

    List<CommentDTO> viewComments(String task);

}
