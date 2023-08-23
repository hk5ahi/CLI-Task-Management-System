package server.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import server.domain.Comment;
import server.domain.Task;
import java.util.List;

public interface CommentDao extends JpaRepository<Comment,String> {
    List<Comment> getCommentByTask(Task task);

}
