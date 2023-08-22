package server.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import server.domain.Task;
import java.util.Optional;

public interface TaskDao extends JpaRepository<Task,String>,CustomTaskDao {

    boolean existsByTitle(String title);
    Optional<Task> findByTitle(String title);



}
