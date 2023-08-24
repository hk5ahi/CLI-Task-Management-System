package server.service;
import server.dto.CommentDTO;
import java.util.List;
public interface CommentService {

    void addCommentByUser(CommentDTO commentDTO,String header);
    List<CommentDTO> getComment(String title, String header);
}
