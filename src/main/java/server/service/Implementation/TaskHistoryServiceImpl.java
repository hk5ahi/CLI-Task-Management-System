package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.TaskDao;
import server.domain.Task;
import server.domain.TaskHistory;
import server.dto.TaskHistoryDTO;
import server.service.TaskHistoryService;
import server.service.TaskService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class TaskHistoryServiceImpl implements TaskHistoryService {

    private final TaskDao taskDao;

    public TaskHistoryServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;

    }

    @Override
    public TaskHistoryDTO viewTaskHistory(String title) {
        Optional<Task> optionalTask = taskDao.getTaskByTitle(title);
        if (optionalTask.isPresent()) {
            Task task=optionalTask.get();
            TaskHistory history = task.getHistory();

            Instant timestamp = history.getTimestamp();
            LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = localDateTime.format(formatter);

            String oldStatus = history.getOldStatus().toString();
            String newStatus = history.getNewStatus().toString();
            String movedBy = history.getMovedBy().getFirstName() + " " + history.getMovedBy().getLastName();

            TaskHistoryDTO taskHistoryDTO = new TaskHistoryDTO();
            taskHistoryDTO.setMovedBy(movedBy);
            taskHistoryDTO.setOldStatus(Task.Status.valueOf(oldStatus));
            taskHistoryDTO.setNewStatus(Task.Status.valueOf(newStatus));
            taskHistoryDTO.setMovedAt(formattedDateTime);
            return taskHistoryDTO;

        }

    else
    {
        return null;
    }

}}