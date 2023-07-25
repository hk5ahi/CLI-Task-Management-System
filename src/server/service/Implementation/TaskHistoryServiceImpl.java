package server.service.Implementation;

import server.domain.Task;
import server.service.TaskHistoryService;
import server.utilities.Taskbytitle;

public class TaskHistoryServiceImpl implements TaskHistoryService {

    private Taskbytitle taskbytitle = new Taskbytitle();

    @Override
    public void viewTaskHistory() {
        Task task = taskbytitle.gettaskbytitle();

        System.out.printf("The old status of the task is %s, its new status is %s, the movement occurred at date and time which is %s, and it was moved by %s.%n",
                task.getHistory().getOldStatus(), task.getHistory().getNewStatus(), task.getHistory().getTimestamp(), task.getHistory().getMovedBy().getFirstName() + " " + task.getHistory().getMovedBy().getLastName());
    }


}
