import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Supervisor {
    private String username;
    private String password;
    public String role;
    public String name;

    Supervisor() {
        username = "m.omer";
        password = "Ts121200";
        role = "Supervisor";
    }

    boolean verifyCredentials(String u, String p) {

        if (username.equals(u) && password.equals(p)) {
            return true;
        } else {
            return false;
        }

    }

    void viewallTasks() {

        for (int i = 0; i < Employee.allTasks.size(); i++) {

            System.out.printf("The title of task is %s with its description which is %s whose employee is %s and  its status is %s.It is created by %s", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description, Employee.allTasks.get(i).assignee, Employee.allTasks.get(i).taskStatus, Employee.allTasks.get(i).createdBy);


        }

    }

    void viewbyStatus() {

        for (int i = 0; i < Employee.allTasks.size(); i++) {

            System.out.println("The tasks whose status are CREATED are:");
            if (Employee.allTasks.get(i).taskStatus == "CREATED") {

                System.out.printf("The title of task is %s with its description which is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description);
            }

        }
        System.out.println(" ");

        for (int i = 0; i < Employee.allTasks.size(); i++) {

            System.out.println("The tasks whose status are In Progress are:");
            if (Employee.allTasks.get(i).taskStatus == "IN_PROGRESS") {

                System.out.printf("The title of task is %s with its description which is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description);
            }

        }
        System.out.println(" ");
        for (int i = 0; i < Employee.allTasks.size(); i++) {

            System.out.println("The tasks whose status are In Review are:");
            if (Employee.allTasks.get(i).taskStatus == "IN_REVIEW") {

                System.out.printf("The title of task is %s with its description which is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description);
            }

        }
        System.out.println(" ");
        for (int i = 0; i < Employee.allTasks.size(); i++) {

            System.out.println("The tasks whose status are COMPLETED are:");
            if (Employee.allTasks.get(i).taskStatus == "COMPLETED") {

                System.out.printf("The title of task is %s with its description which is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description);
            }

        }
        System.out.println(" ");

    }

    void viewbyEmp() {
        System.out.println("The tasks are categorized employee-wise with their respective statuses.");

        for (int i = 0; i < User.employees.size(); i++) {

            System.out.printf("The name of Employee is %s and its assigned tasks with their status are:\n", User.employees.get(i).name);

            for (int j = 0; j < Employee.allTasks.size(); j++) {
                if (Employee.allTasks.get(j).assignee.equals(User.employees.get(i).name)) {

                    System.out.printf("The title of task is %s with its description which is %s and its status is %s.", Employee.allTasks.get(j).title, Employee.allTasks.get(j).description, Employee.allTasks.get(j).taskStatus);

                }

            }

        }


    }

    void viewbyManager() {
        System.out.println("The tasks are categorized manager-wise with their respective statuses.");

        for (int i = 0; i < User.managers.size(); i++) {

            System.out.printf("The name of Manager is %s and its created tasks with their status are:\n", User.managers.get(i).name);

            for (int j = 0; j < Employee.allTasks.size(); j++) {
                if (Employee.allTasks.get(j).createdBy.equals(User.managers.get(i).name)) {

                    System.out.printf("The title of task is %s with its description which is %s and its status is %s.", Employee.allTasks.get(j).title, Employee.allTasks.get(j).description, Employee.allTasks.get(j).taskStatus);

                }

            }

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

    void archiveTask() {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : Employee.allTasks) {
            System.out.println(t.title);
        }

        do {
            System.out.println("Enter the title of task that you want to archive.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        task.assigned = false;
        task.assignee = null;
        //status of task !!!!
        //task.taskStatus= String.valueOf(Task.Status.CREATED);

        System.out.println("The task has been archive successfully.");
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

    void viewTaskHistory() {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : Employee.allTasks) {
            System.out.println(t.title);
        }

        do {
            System.out.println("Enter the title of task whose task history you want to see.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        System.out.printf("The old status of task is %s , its new status is %s, the movement occurred at date and time which is %s and its moved by %s", task.old_status, task.new_status, task.timestamp.toString(), task.moved_by);
    }

    void viewallEmp() {
        System.out.println("The Supervisor of the System is:");

        for (int i = 0; i < User.supervisors.size(); i++) {
            System.out.println(User.supervisors.get(i).name);

        }

        System.out.println("The Managers of the System are:");

        for (int i = 0; i < User.managers.size(); i++) {
            System.out.println(User.managers.get(i).name);

        }
        System.out.println("The Employees of the System are:");

        for (int i = 0; i < User.employees.size(); i++) {
            System.out.println(User.employees.get(i).name);

        }


    }
}
