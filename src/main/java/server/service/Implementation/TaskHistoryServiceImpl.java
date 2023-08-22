package server.service.Implementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TaskHistoryServiceImpl implements TaskHistoryService {

    private final TaskDao taskDao;
    private final TaskHistoryDao taskHistoryDao;
    private final UtilityService utilityService;
    private final Logger log = LoggerFactory.getLogger(TaskHistoryServiceImpl.class);

    public TaskHistoryServiceImpl(TaskDao taskDao, TaskHistoryDao taskHistoryDao, UtilityService utilityService) {
        this.taskDao = taskDao;
        this.taskHistoryDao = taskHistoryDao;
        this.utilityService = utilityService;
    }


    @Override
    public List<TaskHistoryDTO> getTaskHistory(String title, String header) {
        if (utilityService.isAuthenticatedSupervisor(header)) {
            return retrieveTaskHistory(title);
        } else {
            log.error("The requesting user is not a Supervisor");
            throw new ForbiddenAccessException("Only Supervisor can view Task History");
        }
    }


    private List<TaskHistoryDTO> retrieveTaskHistory(String title) {
        Optional<Task> optionalTask = taskDao.findByTitle(title);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            List<TaskHistory> histories = taskHistoryDao.findByTask(task);

                List<TaskHistoryDTO> taskHistoryDTOS = new ArrayList<>();
                for (TaskHistory history : histories) {
                    TaskHistoryDTO taskHistoryDTO = createTaskHistoryDTOFromHistory(history);
                    taskHistoryDTOS.add(taskHistoryDTO);
                }

                return taskHistoryDTOS;
            }

        return Collections.emptyList();
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