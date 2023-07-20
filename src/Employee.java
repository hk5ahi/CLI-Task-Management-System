import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Employee extends User {

    public static List<Employee> employees = new ArrayList<>();

    public List<Task> assignedTasks = new ArrayList<>();
    public static List<Task> allTasks = new ArrayList<>();
    LocalDateTime startTime;

    Employee(String firstname, String lastname, String username, String password) {

        this.username = username;
        this.password = password;
        this.first_Name = firstname;
        this.last_Name = lastname;
        employees.add(this);

    }

    Scanner scan = new Scanner(System.in);

    public void empCheck() {
        if (employees.size() == 0) {
            System.out.println("Please first create Employee user from Supervisor.");
        }

    }

    public static Employee findEmployee(String providedUsername, String providedPassword) {
        if (employees.size() != 0) {
            for (Employee employee : employees) {

                if (employee.username.equals(providedUsername) && employee.password.equals(providedPassword)) {
                    return employee; // Return the matched Employee object
                }
            }
            return null; // If no match found, return null
        }
        return null;
    }

    void createdToInprogress() {

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

            task.taskStatus = String.valueOf(Task.Status.IN_PROGRESS);
            task.history.timestamp = LocalDateTime.now();
            startTime = LocalDateTime.now();
            task.history.old_status = "CREATED";
            task.history.new_status = "IN_PROGRESS";
            task.history.moved_by = first_Name + " " + last_Name;

        } else {

            System.out.println("The task is not in desirable state.");
        }
    }

    void InprogressToInreview() {


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
            if (minutes >= Task.total_time) {
                task.taskStatus = String.valueOf(Task.Status.IN_REVIEW);
                task.history.timestamp = LocalDateTime.now();
                task.history.old_status = "IN_PROGRESS";
                task.history.new_status = "IN_REVIEW";
                task.history.moved_by = first_Name + " " + last_Name;

            } else {
                System.out.printf("The minimum time for task to stay in Progress State is %d minutes.", Task.total_time);
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
        c.createdAt = (LocalDateTime.now().toString());
        c.createdBy = first_Name + " " + last_Name;
        ;
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
