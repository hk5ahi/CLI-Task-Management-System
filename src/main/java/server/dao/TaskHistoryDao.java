package server.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import server.domain.TaskHistory;

import java.util.List;
import java.util.Optional;

public interface TaskHistoryDao extends JpaRepository<TaskHistory,String> {
    Optional<List<TaskHistory>> findByTitle(String title) ;

    void updateByTitle(TaskHistory taskHistory, String title) ;


}
