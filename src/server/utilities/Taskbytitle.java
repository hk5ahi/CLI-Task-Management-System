package server.utilities;

import server.dao.implementation.TaskDaoImpl;
import server.domain.Task;
import server.service.Implementation.TaskServiceImpl;

import java.util.Scanner;

public class Taskbytitle {

    TaskServiceImpl taskService = new TaskServiceImpl();
    private TaskDaoImpl taskDao = TaskDaoImpl.getInstance();
    Scanner scan = new Scanner(System.in);

    public Task gettaskbytitle() {

        Task task = null;
        System.out.println("The Tasks are:");

        for (Task t : taskDao.getAllTasksbyEmployee()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task that you want to comment.");
            String title = scan.nextLine();
            task = taskService.getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        return task;
    }

    public String nameofEmployee() {
        System.out.println("Enter the full name of the employee that you want to assign.");
        String name = scan.nextLine();
        return name;

    }

}

