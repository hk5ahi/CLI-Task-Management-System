package server.utilities;

import server.dao.implementation.TaskDaoImpl;
import server.domain.Employee;
import server.domain.Task;
import server.service.Implementation.TaskServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Taskbytitle {

    TaskServiceImpl taskService;
    private TaskDaoImpl taskDao = TaskDaoImpl.getInstance();
    Scanner scan = new Scanner(System.in);

    public Task gettaskbytitle() {
        Task task;


        List<Task> allTasks = taskDao.getAllTasksbyEmployee();
        if (allTasks.isEmpty()) {
            System.out.println("No tasks yet.");
            return null; // Return null to indicate that no tasks are available
        }
        System.out.println("The Tasks are:");

        for (Task t : allTasks) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task.");
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


    public Task getAssignTaskByTitle(Employee employee) {
        Task assignedTask;
        boolean hasAssignedTasks = false;

        System.out.println("The assigned tasks for the employee are:");

        for (Task task : taskDao.getAllTasksbyEmployee()) {
            String assigneeFullName = task.getAssignee() != null
                    ? task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName()
                    : "N/A";

            if (assigneeFullName.equals(employee.getFirstName() + " " + employee.getLastName())) {
                System.out.println(task.getTitle());
                hasAssignedTasks = true;
            }
        }

        if (!hasAssignedTasks) {
            System.out.println("No Tasks.");
            return null;
        }

        do {
            System.out.println("Enter the title of the task.");
            String title = scan.nextLine();
            assignedTask = taskService.getTaskByTitle(title);

            if (assignedTask == null) {
                System.out.println("Wrong title entered");
            }
        } while (assignedTask == null);

        return assignedTask;
    }

}



