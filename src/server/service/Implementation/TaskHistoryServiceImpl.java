package server.service.Implementation;

import server.dao.EmployeeDao;
import server.dao.implementation.EmployeeDaoImpl;
import server.domain.Task;
import server.service.TaskHistoryService;

import java.util.Scanner;

public class TaskHistoryServiceImpl implements TaskHistoryService {

    TaskServiceImpl taskService = new TaskServiceImpl();

    @Override
    public void viewTaskHistory() {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        for (Task t : employeeDao.getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task whose task history you want to see.");
            String title = scan.nextLine();
            task = taskService.getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        System.out.printf("The old status of task is %s , its new status is %s, the movement occurred at date and time which is %s and its moved by %s", task.getHistory().getOldStatus(), task.getHistory().getNewStatus(), task.getHistory().getTimestamp(), task.getHistory().getMovedBy());
    }


}
