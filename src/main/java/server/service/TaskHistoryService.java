package server.service;

import server.dto.TaskHistoryDTO;

public interface TaskHistoryService {

    TaskHistoryDTO viewTaskHistory(String title);
}
