package server.service.Implementation;

import server.domain.Task;
import server.service.TaskHistoryService;
import server.utilities.Taskbytitle;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TaskHistoryServiceImpl implements TaskHistoryService {

    private Taskbytitle taskbytitle = new Taskbytitle();

    @Override
    public void viewTaskHistory() {
        Task task = taskbytitle.gettaskbytitle();
        Instant timestamp = task.getHistory().getTimestamp();
        LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Define the desired date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime to the desired format
        String formattedDateTime = localDateTime.format(formatter);

        // Print the formatted date and time
        System.out.printf("The old status of the task is %s, its new status is %s, the movement occurred at date and time which is %s, and it was moved by %s.%n",
                task.getHistory().getOldStatus(), task.getHistory().getNewStatus(), formattedDateTime, task.getHistory().getMovedBy().getFirstName() + " " + task.getHistory().getMovedBy().getLastName());
    }
}
