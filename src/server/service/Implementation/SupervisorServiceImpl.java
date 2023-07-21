package server.service.Implementation;

import server.domain.*;
import server.service.SupervisorService;

import java.time.LocalDateTime;
import java.util.Scanner;

import static server.domain.Employee.getAllTasks;
import static server.domain.Employee.getEmployees;
import static server.domain.Manager.getManagers;
import static server.domain.Supervisor.getInstance;


public class SupervisorServiceImpl implements SupervisorService {


    @Override
    public Supervisor verifyCredentials(String providedUsername, String providedPassword) {
        if (getInstance().getUsername().equals(providedUsername) && getInstance().getPassword().equals(providedPassword)) {
            return getInstance(); // Return the current Supervisor object
        } else {
            return null; // If the credentials don't match, return null
        }
    }

    @Override
    public void viewAllTasks() {

        for (int i = 0; i < getAllTasks().size(); i++) {

            System.out.printf("The title of task is %s with its description which is %s whose employee is %s and  its status is %s.It is created by %s", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription(), getAllTasks().get(i).getAssignee(), getAllTasks().get(i).getTaskStatus(), getAllTasks().get(i).getCreatedBy());


        }

    }


    @Override
    public void viewTasksByStatus() {

        for (int i = 0; i < getAllTasks().size(); i++) {

            System.out.println("The tasks whose status are CREATED are:");
            if (getAllTasks().get(i).getTaskStatus().equals("CREATED")) {

                System.out.printf("The title of task is %s with its description which is %s.", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription());
            }

        }
        System.out.println(" ");

        for (int i = 0; i < getAllTasks().size(); i++) {

            System.out.println("The tasks whose status are In Progress are:");
            if (getAllTasks().get(i).getTaskStatus().equals("IN_PROGRESS")) {

                System.out.printf("The title of task is %s with its description which is %s.", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription());
            }

        }
        System.out.println(" ");
        for (int i = 0; i < getAllTasks().size(); i++) {

            System.out.println("The tasks whose status are In Review are:");
            if (getAllTasks().get(i).getTaskStatus().equals("IN_REVIEW")) {

                System.out.printf("The title of task is %s with its description which is %s.", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription());
            }

        }
        System.out.println(" ");
        for (int i = 0; i < getAllTasks().size(); i++) {

            System.out.println("The tasks whose status are COMPLETED are:");
            if (getAllTasks().get(i).getTaskStatus().equals("COMPLETED")) {

                System.out.printf("The title of task is %s with its description which is %s.", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription());
            }

        }
        System.out.println(" ");

    }


    @Override
    public void viewTasksByEmployee() {
        System.out.println("The tasks are categorized employee-wise with their respective statuses.");

        for (int i = 0; i < getEmployees().size(); i++) {

            System.out.printf("The name of Employee is %s %s and its assigned tasks with their status are:\n", getEmployees().get(i).getFirstName(), getEmployees().get(i).getLastName());

            for (int j = 0; j < getAllTasks().size(); j++) {
                if (getAllTasks().get(j).getAssignee().equals(getEmployees().get(i).getFirstName() + " " + getEmployees().get(i).getLastName())) {

                    System.out.printf("The title of task is %s with its description which is %s and its status is %s.", getAllTasks().get(j).getTitle(), getAllTasks().get(j).getDescription(), getAllTasks().get(j).getTaskStatus());

                }

            }

        }


    }

    @Override
    public void viewTasksByManager() {
        System.out.println("The tasks are categorized manager-wise with their respective statuses.");

        for (int i = 0; i < getManagers().size(); i++) {

            System.out.printf("The name of Manager is %s %s and its created tasks with their status are:\n", getManagers().get(i).getFirstName(), getManagers().get(i).getLastName());

            for (int j = 0; j < getAllTasks().size(); j++) {
                if (getAllTasks().get(j).getCreatedBy().equals(getManagers().get(i).getFirstName() + " " + getManagers().get(i).getLastName())) {

                    System.out.printf("The title of task is %s with its description which is %s and its status is %s.", getAllTasks().get(j).getTitle(), getAllTasks().get(j).getDescription(), getAllTasks().get(j).getTaskStatus());

                }

            }

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

    public void archiveTask() {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task that you want to archive.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        task.setAssigned(false);
        task.setAssignee(null);
        //status of task !!!!
        //task.taskStatus= String.valueOf(server.domain.Task.Status.CREATED);

        System.out.println("The task has been archive successfully.");

    }
    @Override
    public void addComments(String message) {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
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
        c.setBody(message);
        c.setCreatedAt(LocalDateTime.now().toString());
        c.setCreatedBy(getInstance().getFirstName() + " " + getInstance().getLastName());

        task.getComments().add(c);
    }

    @Override
    public void viewTaskHistory() {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task whose task history you want to see.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        System.out.printf("The old status of task is %s , its new status is %s, the movement occurred at date and time which is %s and its moved by %s", task.getHistory().getOldStatus(), task.getHistory().getNewStatus(), task.getHistory().getTimestamp(), task.getHistory().getMovedBy());
    }

    @Override
    public void viewSupervisor() {
        System.out.println("The Supervisor of the System is: ");
        System.out.println(getInstance().getFirstName() + " " + getInstance().getLastName());

    }

    @Override
    public void viewAllEmployees() {
        System.out.println("The Employees of the System are:");

        for (int i = 0; i < getEmployees().size(); i++) {
            System.out.println(getEmployees().get(i).getFirstName() + " " + getEmployees().get(i).getLastName());

        }

    }

    @Override
    public void viewAllManagers() {

        System.out.println("The Managers of the System are:");

        for (int i = 0; i < getManagers().size(); i++) {
            System.out.println(getManagers().get(i).getFirstName() + " " + getManagers().get(i).getLastName());

        }

    }

    @Override
    public void createEmployee(String firstname, String lastname, String username, String password) {
        Employee e = new Employee(firstname, lastname, username, password);
        System.out.println("Employee created successfully.");

    }

    @Override
    public void createManager(String firstname, String lastname, String username, String password) {

        Manager m = new Manager(firstname, lastname, username, password);
        System.out.println("server.domain.Manager created successfully.");
    }
}
