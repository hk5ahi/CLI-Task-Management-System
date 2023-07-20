import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Manager extends User {

    public static List<Manager> managers = new ArrayList<>();

    Manager(String firstname, String lastname, String username, String password) {

        this.username = username;
        this.password = password;
        this.first_Name = firstname;
        this.last_Name = lastname;
        managers.add(this);


    }


    public static Manager findManager(String providedUsername, String providedPassword) {
        for (Manager manager : managers) {
            if (manager.username.equals(providedUsername) && manager.password.equals(providedPassword)) {
                return manager; // Return the matched Employee object
            }
        }
        return null; // If no match found, return null
    }

    void createTask(String title, String description, int total_time) {
        Task t = new Task(title, description, total_time);
        t.createdBy = first_Name + " " + last_Name;
        ;
        t.createdAt = LocalTime.now().toString();
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
        for (Employee employee : Employee.employees) {
            System.out.println(employee.first_Name + " " + employee.last_Name);
        }

        // Loop until the correct name is entered and it's not the manager's name
        do {
            System.out.println("Enter the full name of the employee that you want to assign.");
            String name = scan.nextLine();
            name11 = getEmployeeByName(name);

            // Check if the selected employee is the manager
            if (name11.equals(" ")) {
                // If the name is valid and not the manager's name, proceed with task assignment
                System.out.println("The task can not be assigned to manager.");

            } else if (name11 != null && !name11.equals(" ")) {
                // If the name is the manager's name, display a message and re-loop to get a different name
                break;
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
                System.out.printf("The Task titled %s is assigned to %s.\n", title1, name11);
                // Add the task to the assigned tasks list of the selected employee
                for (Employee employee : Employee.employees) {
                    String name = employee.first_Name + " " + employee.last_Name;
                    if (name.equalsIgnoreCase(name11)) {
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

        for (Manager manager : Manager.managers) {
            String name0 = manager.first_Name + " " + manager.last_Name;
            if (name0.equalsIgnoreCase(name)) {

                return " ";
            }
        }

        for (Employee employee : Employee.employees) {
            String name0 = employee.first_Name + " " + employee.last_Name;
            if (name0.equalsIgnoreCase(name)) {
                return employee.first_Name + " " + employee.last_Name;
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
            task.history.timestamp = LocalDateTime.now();
            task.history.old_status = "IN_REVIEW";
            task.history.new_status = "COMPLETED";
            task.history.moved_by = first_Name + " " + last_Name;


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
        c.createdAt = LocalDateTime.now().toString();
        c.createdBy = first_Name + " " + last_Name;
        task.comments.add(c);
        System.out.println("The comment has been added successfully.");
    }

    void viewallTasks() {
        System.out.println("The tasks which are created by Manager himself are: ");
        for (int i = 0; i < Employee.allTasks.size(); i++) {
            if (Employee.allTasks.get(i).createdBy.equals(this.first_Name + " " + this.last_Name)) {
                System.out.printf("The title of task is %s with its description which is %s.\n", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description);
            }

        }


    }

    void viewallTasksbyEmp() {

        for (int i = 0; i < Employee.allTasks.size(); i++) {
            if (Employee.allTasks.get(i).createdBy.equals(this.first_Name + " " + this.last_Name)) {

                System.out.printf("The title of task is %s with its description which is %s and its employee is %s.\n", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description, Employee.allTasks.get(i).assignee);
            }

        }

    }

    void viewallTasksbyStat() {
        for (int i = 0; i < Employee.allTasks.size(); i++) {
            if (Employee.allTasks.get(i).createdBy.equals(this.first_Name + " " + this.last_Name)) {

                System.out.printf("The title of task is %s with its description which is %s and its status is %s.\n", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description, Employee.allTasks.get(i).taskStatus);
            }

        }

    }

    void viewallTasksbyEmpandStat() {

        for (int i = 0; i < Employee.allTasks.size(); i++) {
            if (Employee.allTasks.get(i).createdBy.equals(this.first_Name + " " + this.last_Name)) {

                System.out.printf("The title of task is %s with its description which is %s whose employee is %s and  its status is %s.\n", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description, Employee.allTasks.get(i).assignee, Employee.allTasks.get(i).taskStatus);
            }

        }

    }
}
