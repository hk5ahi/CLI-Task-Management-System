package server.service.Implementation;

import server.domain.Employee;
import server.domain.Task;
import server.service.EmployeeService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

import static server.domain.Employee.getEmployees;

public class EmployeeServiceImpl implements EmployeeService {

    private static final Scanner scan = new Scanner(System.in);

    public void empCheck() {
        if (getEmployees().size() == 0) {
            System.out.println("Please first create Employee user from Supervisor.");
        }

    }


    public void createdToInProgress(String username, String title) {

        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : Employee.getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task whose status you want to change.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        if (task.getTaskStatus().equals("CREATED")) {

            task.setTaskStatus(String.valueOf(Task.Status.IN_PROGRESS));
            task.getHistory().setTimestamp(LocalDateTime.now().toString());


            /////////////////////////////////////////////////////////////////////till here
            startTime = LocalDateTime.now();
            task.history.old_status = "CREATED";
            task.history.new_status = "IN_PROGRESS";
            task.history.moved_by = first_Name + " " + last_Name;

        } else {

            System.out.println("The task is not in desirable state.");
        }
    }

    @Override
    public void inProgressToInReview(String username, String title) {
        Employee employee = findEmployee(username, null);
        if (employee == null) {
            System.out.println("Invalid username!");
            return;
        }

        Task task = getTaskByTitle(title);
        if (task == null) {
            System.out.println("Invalid task title!");
            return;
        }

        if (task.getTaskStatus().equals("IN_PROGRESS")) {
            LocalDateTime endTime = LocalDateTime.now();
            Duration duration = Duration.between(task.getStartTime(), endTime);
            long minutes = duration.toMinutes();
            if (minutes >= Task.total_time) {
                task.setTaskStatus(String.valueOf(Task.Status.IN_REVIEW));
                task.getHistory().setTimestamp(LocalDateTime.now());
                task.getHistory().setOldStatus("IN_PROGRESS");
                task.getHistory().setNewStatus("IN_REVIEW");
                task.getHistory().setMovedBy(employee.getFirstName() + " " + employee.getLastName());
            } else {
                System.out.printf("The minimum time for the task to stay in the Progress state is %d minutes.", Task.total_time);
            }
        } else {
            System.out.println("The task is not in a desirable state.");
        }
    }

    @Override
    public void addComments(String username, String title, String message) {
        Employee employee = findEmployee(username, null);
        if (employee == null) {
            System.out.println("Invalid username!");
            return;
        }

        Task task = getTaskByTitle(title);
        if (task == null) {
            System.out.println("Invalid task title!");
            return;
        }

        Comment c = new Comment();
        c.setBody(message);
        c.setCreatedAt(LocalDateTime.now().toString());
        c.setCreatedBy(employee.getFirstName() + " " + employee.getLastName());
        task.getComments().add(c);
    }

    @Override
    public void viewAssignedTasks(String username) {
        Employee employee = findEmployee(username, null);
        if (employee == null) {
            System.out.println("Invalid username!");
            return;
        }

        System.out.print("The assigned tasks for the employee are:\n");
        for (Task task : employee.getAssignedTasks()) {
            System.out.print(task.getDescription());
        }
    }

    @Override
    public void viewAllTasks() {
        System.out.println("The all tasks with Status are:");
        for (Task task : Employee.allTasks) {
            System.out.printf("The title of task is %s with its description which is %s and its status is %s%n",
                    task.getTitle(), task.getDescription(), task.getTaskStatus());
        }
    }

    private Task getTaskByTitle(String title) {
        for (Task task : Employee.allTasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null; // Task not found
    }
}
