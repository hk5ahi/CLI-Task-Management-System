package server.service;


import server.dto.TaskHistoryDTO;

import java.util.List;
import java.util.Optional;

public interface TaskHistoryService {

    Optional<List<TaskHistoryDTO>> getTaskHistory(String title);

    Optional<List<TaskHistoryDTO>> getTaskHistoryByController(String title,String header);
}
