package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.TaskDao;
import server.dao.TaskHistoryDao;
import server.domain.Task;
import server.domain.TaskHistory;
import server.dto.TaskHistoryDTO;
import server.exception.ForbiddenAccessException;
import server.service.TaskHistoryService;
import server.utilities.UtilityService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskHistoryServiceImpl implements TaskHistoryService {

    private final TaskDao taskDao;
    private final TaskHistoryDao taskHistoryDao;
    private final UtilityService utilityService;

    public TaskHistoryServiceImpl(TaskDao taskDao, TaskHistoryDao taskHistoryDao, UtilityService utilityService) {
        this.taskDao = taskDao;

        this.taskHistoryDao = taskHistoryDao;
        this.utilityService = utilityService;
    }


    @Override
    public Optional<List<TaskHistoryDTO>> getTaskHistoryByController(String title, String header) {
        if (utilityService.isAuthenticatedSupervisor(header)) {
            return getTaskHistory(title);
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @Override
    public Optional<List<TaskHistoryDTO>> getTaskHistory(String title) {
        Optional<Task> optionalTask = taskDao.getTaskByTitle(title);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            Optional<List<TaskHistory>> histories = taskHistoryDao.getTaskHistory(task.getTitle());

            if (histories.isPresent()) {
                List<TaskHistoryDTO> taskHistoryDTOS = new ArrayList<>();

                for (TaskHistory history : histories.get()) {
                    TaskHistoryDTO taskHistoryDTO = createTaskHistoryDTOFromHistory(history);
                    taskHistoryDTOS.add(taskHistoryDTO);
                }

                return taskHistoryDTOS.isEmpty() ? Optional.empty() : Optional.of(taskHistoryDTOS);
            }
        }

        return Optional.empty();
    }

    private TaskHistoryDTO createTaskHistoryDTOFromHistory(TaskHistory history) {
        Instant timestamp = history.getTimestamp();
        String oldStatus = history.getOldStatus().toString();
        String newStatus = history.getNewStatus().toString();
        String movedBy = history.getMovedBy().getFirstName() + " " + history.getMovedBy().getLastName();

        TaskHistoryDTO taskHistoryDTO = new TaskHistoryDTO();
        taskHistoryDTO.setMovedBy(movedBy);
        taskHistoryDTO.setOldStatus(Task.Status.valueOf(oldStatus));
        taskHistoryDTO.setNewStatus(Task.Status.valueOf(newStatus));
        taskHistoryDTO.setMovedAt(timestamp);

        return taskHistoryDTO;
    }

}