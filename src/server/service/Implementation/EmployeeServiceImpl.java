package server.service.Implementation;

import server.domain.Comment;
import server.domain.Employee;
import server.domain.Task;
import server.service.EmployeeService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

import static server.domain.Employee.getAllTasks;
import static server.domain.Employee.getEmployees;

public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public void empCheck() {
        if (getEmployees().size() == 0) {
            System.out.println("Please first create Employee user from Supervisor.");
        }

    }

    @Override
    public Employee findEmployee(String providedUsername, String providedPassword) {

        if (getEmployees().size() != 0) {
            for (Employee employee : getEmployees()) {

                if (employee.getUsername().equals(providedUsername) && employee.getPassword().equals(providedPassword)) {
                    return employee; // Return the matched Employee object
                }
            }
            return null; // If no match found, return null
        }
        return null;
    }


    @Override
    public void createdToInProgress(Employee employee) {

        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task whose status you want to change.");
            Scanner scan = new Scanner(System.in);
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        if (task.getTaskStatus().equals("CREATED")) {

            task.setTaskStatus(String.valueOf(Task.Status.IN_PROGRESS));
            task.getHistory().setTimestamp(LocalDateTime.now().toString());
            task.setStartTime(LocalDateTime.now());
            task.getHistory().setOldStatus("CREATED");
            task.getHistory().setNewStatus("IN_PROGRESS");
            task.getHistory().setMovedBy(employee.getFirstName() + " " + employee.getLastName());

        } else {

            System.out.println("The task is not in desirable state.");
        }
    }

    @Override
    public void inProgressToInReview(Employee employee) {
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task whose status you want to change.");
            Scanner scan = new Scanner(System.in);
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        if (task.getTaskStatus().equals("IN_PROGRESS")) {
            LocalDateTime endTime = LocalDateTime.now();
            Duration duration = Duration.between(task.getStartTime(), endTime);
            long minutes = duration.toMinutes();
            if (minutes >= task.getTotal_time()) {
                task.setTaskStatus(String.valueOf(Task.Status.IN_REVIEW));
                task.getHistory().setTimestamp(LocalDateTime.now().toString());
                task.getHistory().setOldStatus("IN_PROGRESS");
                task.getHistory().setNewStatus("IN_REVIEW");
                task.getHistory().setMovedBy(employee.getFirstName() + " " + employee.getLastName());

            } else {
                System.out.printf("The minimum time for task to stay in Progress State is %d minutes.", task.getTotal_time());
            }
        } else {
            System.out.println("The task is not in desirable state.");

        }
    }

    @Override
    public Task getTaskByTitle(String title) {
        for (Task task : getAllTasks()) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null; // Task not found
    }

    @Override
    public void addComments(String message, Employee employee) {
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task that you want to comment.");
            Scanner scan = new Scanner(System.in);
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        Comment c = new Comment();
        c.setBody(message);
        c.setCreatedAt((LocalDateTime.now().toString()));
        c.setCreatedBy(employee.getFirstName() + " " + employee.getLastName());

        task.addComment(c);
        System.out.printf("The comment has been added successfully by %s.\n", employee.getFirstName() + " " + employee.getLastName());
    }

    @Override
    public void viewAssignedTasks(Employee employee) {
        System.out.print("The assigned tasks for the employee are:\n");
        for (int i = 0; i < employee.getAssignedTasks().size(); i++) {
            System.out.print(employee.getAssignedTasks().get(i).getDescription());
        }
    }

    @Override
    public void viewAllTasks() {
        System.out.println("The all tasks with Status are:");
        for (int i = 0; i < getAllTasks().size(); i++) {
            System.out.printf("The title of task is %s with its description which is %s and its status is %s", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription(), getAllTasks().get(i).getTaskStatus());

        }
    }


}



