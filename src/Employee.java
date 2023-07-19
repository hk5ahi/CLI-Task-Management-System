import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Employee extends Task {
    private String username;
    public String name;
    public String role;
    private String password;

    public List<Task> assignedTasks;
    public static List<Task> allTasks;
    LocalDateTime startTime;

    Employee() {

        username = "m.hanan";
        password = "Ts121212";
        role = "Employee";

    }

    boolean verifyCredentials(String u, String p) {

        if (username.equals(u) && password.equals(p)) {
            return true;
        } else {
            return false;
        }

    }

    void createdToInprogress() {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : Employee.allTasks) {
            System.out.println(t.title);
        }

        do {
            System.out.println("Enter the title of task whose status you want to change.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        if (task.taskStatus.equals("CREATED")) {

            task.taskStatus = String.valueOf(Status.IN_PROGRESS);
            task.timestamp = LocalDateTime.now();
            startTime = LocalDateTime.now();
            task.old_status = "CREATED";
            task.new_status = "IN_PROGRESS";
            task.moved_by = name;

        } else {

            System.out.println("The task is not in desirable state.");
        }
    }

    void InprogressToInreview() {

        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : Employee.allTasks) {
            System.out.println(t.title);
        }

        do {
            System.out.println("Enter the title of task whose status you want to change.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        if (task.taskStatus.equals("IN_PROGRESS")) {
            LocalDateTime endTime = LocalDateTime.now();
            Duration duration = Duration.between(startTime, endTime);
            long minutes = duration.toMinutes();
            if (minutes >= total_time) {
                task.taskStatus = String.valueOf(Status.IN_REVIEW);
                task.timestamp = LocalDateTime.now();
                task.old_status = "IN_PROGRESS";
                task.new_status = "IN_REVIEW";
                task.moved_by = name;

            } else {
                System.out.printf("The minimum time for task to stay in Progress State is %d minutes.", total_time);
            }
        } else {
            System.out.println("The task is not in desirable state.");

        }
    }

    private Task getTaskByTitle(String title) {
        for (Task task : Employee.allTasks) {
            if (task.title.equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null; // Task not found
    }

    void addComments(String message) {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : Employee.allTasks) {
            System.out.println(t.title);
        }

        do {
            System.out.println("Enter the title of task that you want to comment.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        Comment c = new Comment();
        c.body = message;
        c.createdAt = LocalTime.from(LocalDateTime.now());
        c.createdBy = name;
        task.comments.add(c);
    }

    void viewAssignedtasks() {
        System.out.print("The assigned tasks for the employee are:\n");
        for (int i = 0; i < assignedTasks.size(); i++) {
            System.out.print(assignedTasks.get(i).description);
        }
    }

    void allTasks() {
        System.out.println("The all tasks with Status are:");
        for (int i = 0; i < allTasks.size(); i++) {
            System.out.printf("The title of task is %s with its description which is %s and its status is %s", allTasks.get(i).title, allTasks.get(i).description, allTasks.get(i).taskStatus);

        }
    }


}
