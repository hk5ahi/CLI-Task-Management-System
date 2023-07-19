import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Manager {

    private String username;
    private String password;
    public String role;
    public String name;

    Manager() {
        username = "m.ali";
        password = "Ts121211";
        role = "Manager";
    }

    boolean verifyCredentials(String u, String p) {

        if (username.equals(u) && password.equals(p)) {
            return true;
        } else {
            return false;
        }

    }

    void createTask(String title, String description, int total_time) {
        Task t = new Task(title, description, total_time);
        t.createdBy = name;
        t.createdAt = LocalDateTime.from(LocalTime.now());
        Employee.allTasks.add(t);
    }

    void assignTask() {
        String title1;
        String name11 = null;
        Task task = null;
        Scanner scan = new Scanner(System.in);

        System.out.println("The Tasks are:");
        for (Task t : Employee.allTasks) {
            System.out.println(t.title);
        }

        // Loop until the correct title is entered
        do {
            System.out.println("Enter the title of the task that you want to assign.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        title1 = task.title;

        System.out.println("The Employees are:");
        for (Employee employee : User.employees) {
            System.out.println(employee.name);
        }

        // Loop until the correct name is entered and it's not the manager's name
        do {
            System.out.println("Enter the name of the employee that you want to assign.");
            String name = scan.nextLine();
            name11 = getEmployeeByName(name);

            // Check if the selected employee is the manager
            if (name11 != null && !name11.equalsIgnoreCase(this.name)) {
                // If the name is valid and not the manager's name, proceed with task assignment
                break;
            } else if (name11 != null) {
                // If the name is the manager's name, display a message and re-loop to get a different name
                System.out.println("Cannot assign the task to yourself. Please select a different employee.");
            } else {
                // If the name is not found, display a message and re-loop to get a different name
                System.out.println("Wrong name entered");
            }
        } while (true);

        if (task != null && name11 != null) {

            if (task.assigned) {
                System.out.println("Already assigned task.");
            } else {
                task.assignee = name11;
                task.assigned = true;

                // Add the task to the assigned tasks list of the selected employee
                for (Employee employee : User.employees) {
                    if (employee.name.equalsIgnoreCase(name11)) {
                        employee.assignedTasks.add(task);
                        break;
                    }
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

    private String getEmployeeByName(String name) {
        for (Employee employee : User.employees) {
            if (employee.name.equalsIgnoreCase(name)) {
                return employee.name;
            }
        }
        return null; // Employee not found
    }

    void in_reviewtoCompleted() {

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

        if (task.taskStatus.equals("IN_REVIEW")) {
            task.taskStatus = String.valueOf(Task.Status.COMPLETED);
            task.timestamp = LocalDateTime.now();
            task.old_status = "IN_REVIEW";
            task.new_status = "COMPLETED";
            task.moved_by = name;


        } else {

            System.out.println("The task is not in desirable state.");
        }


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

    void viewallTasks() {
        System.out.println("The tasks which are created by Manager himself are: ");
        for (int i = 0; i < Employee.allTasks.size(); i++) {
            if (Employee.allTasks.get(i).createdBy == this.name) {
                System.out.printf("The title of task is %s with its description which is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description);
            }

        }


    }

    void viewallTasksbyEmp() {

        for (int i = 0; i < Employee.allTasks.size(); i++) {
            if (Employee.allTasks.get(i).createdBy == this.name) {

                System.out.printf("The title of task is %s with its description which is %s and its employee is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description, Employee.allTasks.get(i).assignee);
            }

        }

    }

    void viewallTasksbyStat() {
        for (int i = 0; i < Employee.allTasks.size(); i++) {
            if (Employee.allTasks.get(i).createdBy == this.name) {

                System.out.printf("The title of task is %s with its description which is %s and its status is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description, Employee.allTasks.get(i).taskStatus);
            }

        }

    }

    void viewallTasksbyEmpandStat() {

        for (int i = 0; i < Employee.allTasks.size(); i++) {
            if (Employee.allTasks.get(i).createdBy == this.name) {

                System.out.printf("The title of task is %s with its description which is %s whose employee is %s and  its status is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description, Employee.allTasks.get(i).assignee, Employee.allTasks.get(i).taskStatus);
            }

        }

    }
}
