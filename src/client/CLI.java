package client;

import server.domain.Employee;
import server.domain.Manager;
import server.domain.Supervisor;
import server.service.CommentService;
import server.service.Implementation.CommentServiceImpl;

import java.util.Scanner;

public class ClientApp {
    CommentService commentService = new CommentServiceImpl();

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the CLI Task Management System!!!");

        // Supervisor login
        System.out.println("Welcome to Supervisor Login System");
        System.out.println("Hey Supervisor!!");
        System.out.println("Please Enter the Credentials:");

        // Variables to store supervisor username and password
        String supervisorUsername, supervisorPassword;
        Supervisor supervisor;

        // Loop until the supervisor credentials are verified
        do {
            System.out.print("Username: ");
            supervisorUsername = scanner.nextLine();
            System.out.print("Password: ");
            supervisorPassword = scanner.nextLine();

            supervisor = Supervisor.verifyCredentials(supervisorUsername, supervisorPassword);

            if (supervisor != null) {
                System.out.println("Credentials verified. Access granted!");
                break;
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        } while (true);

        boolean employeeCreated = false;
        boolean managerCreated = false;

        do {
            System.out.println("Press 1 to create an employee user");
            System.out.println("Press 2 to create a manager user");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                System.out.println("Please enter the First Name.");
                String firstName = scanner.nextLine();
                System.out.println("Please enter the Last Name.");
                String lastName = scanner.nextLine();
                System.out.println("Please enter the username.");
                String username = scanner.nextLine();
                System.out.println("Please enter the password.");
                String password = scanner.nextLine();
                Supervisor.createEmployee(firstName, lastName, username, password);
                employeeCreated = true;
            } else if (choice == 2) {
                System.out.println("Please enter the First Name.");
                String firstName = scanner.nextLine();
                System.out.println("Please enter the Last Name.");
                String lastName = scanner.nextLine();
                System.out.println("Please enter the username.");
                String username = scanner.nextLine();
                System.out.println("Please enter the password.");
                String password = scanner.nextLine();
                Supervisor.createManager(firstName, lastName, username, password);
                managerCreated = true;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        } while (!employeeCreated || !managerCreated);

        int input;

        while (true) {
            System.out.println("\nChoose a role:");
            System.out.println("1. Employee");
            System.out.println("2. Manager");
            System.out.println("3. Exit");
            input = Integer.parseInt(scanner.nextLine());

            switch (input) {
                case 1:
                    System.out.println("Hey Employee!!");
                    System.out.println("Please Enter the Credentials:");

                    // Variables to store employee username and password
                    String empUsername, empPassword;
                    Employee authenticatedEmployee;

                    // Loop until the employee credentials are verified
                    do {
                        System.out.print("Username: ");
                        empUsername = scanner.nextLine();
                        System.out.print("Password: ");
                        empPassword = scanner.nextLine();

                        authenticatedEmployee = Employee.findEmployee(empUsername, empPassword);

                        if (authenticatedEmployee != null) {
                            System.out.println("Credentials verified. Access granted!");
                            // Perform actions for authenticatedEmployee here
                            break;
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                    } while (true);

                    // Working of employee here
                    authenticatedEmployee.viewAssignedtasks();
                    authenticatedEmployee.allTasks();
                    break;

                case 2:
                    System.out.println("Hey Manager!!");
                    System.out.println("Please Enter the Credentials:");

                    // Variables to store manager username and password
                    String mgrUsername, mgrPassword;
                    Manager authenticatedManager;

                    // Loop until the manager credentials are verified
                    do {
                        System.out.print("Username: ");
                        mgrUsername = scanner.nextLine();
                        System.out.print("Password: ");
                        mgrPassword = scanner.nextLine();

                        authenticatedManager = Manager.findManager(mgrUsername, mgrPassword);

                        if (authenticatedManager != null) {
                            System.out.println("Credentials verified. Access granted!");
                            // Perform actions for authenticatedManager here
                            break;
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                    } while (true);

                    // Working of manager here
                    System.out.print("Please enter title for task to create.\n");
                    String taskTitle = scanner.nextLine();
                    authenticatedManager.createTask(taskTitle, "Understand the situation and code it", 2);
                    System.out.print("Please enter title for task to create.\n");
                    String taskTitle2 = scanner.nextLine();
                    authenticatedManager.createTask(taskTitle2, "Debug the errors and remove it", 2);
                    authenticatedManager.assignTask();
                    authenticatedManager.viewallTasksbyEmpandStat();
                    break;

                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid input");
            }
        }
    }
}
