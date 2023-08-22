package server.service;
import server.dto.TaskHistoryDTO;
import java.util.List;


public interface TaskHistoryService {

    List<TaskHistoryDTO> getTaskHistory(String title,String header);
}
