package server.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import server.domain.Task;
import server.domain.TaskHistory;

import java.util.List;


public interface TaskHistoryDao extends JpaRepository<TaskHistory,String> {
    List<TaskHistory> findByTask(Task task) ;


}
