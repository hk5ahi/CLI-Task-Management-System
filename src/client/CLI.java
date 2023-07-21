package client;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Supervisor;
import server.service.EmployeeService;
import server.service.Implementation.EmployeeServiceImpl;
import server.service.Implementation.ManagerServiceImpl;
import server.service.Implementation.SupervisorServiceImpl;
import server.service.ManagerService;
import server.service.SupervisorService;

import java.util.Scanner;

public class CLI {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the CLI server.domain.Task Management System!!!");
        System.out.println("Welcome to server.domain.Supervisor Login System");
        System.out.println("You have to create at-least one employee and manager user to access the employee and manager login system.");
        System.out.println("Hey server.domain.Supervisor!!");
        System.out.println("Please Enter the Credentials:");

        // Variables to store username and password
        String u22;
        String p22;
        SupervisorService sup = new SupervisorServiceImpl();
        Supervisor sup1 = null;
        // Loop until the credentials are verified
        do {
            System.out.print("Username: ");
            u22 = scanner.nextLine();
            System.out.print("Password: ");
            p22 = scanner.nextLine();

            sup1 = sup.verifyCredentials(u22, p22);

            if (sup1 != null) {
                System.out.println("Credentials verified. Access granted!");
                // Perform actions for authenticatedEmployee here
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
            int n = Integer.parseInt(scanner.nextLine());

            if (n == 1) {
                System.out.println("Please enter the First Name.");
                String f = scanner.nextLine();
                System.out.println("Please enter the Last Name.");
                String l = scanner.nextLine();
                System.out.println("Please enter the username.");
                String u = scanner.nextLine();
                System.out.println("Please enter the password.");
                String p = scanner.nextLine();
                sup.createEmployee(f, l, u, p);
                employeeCreated = true;
            } else if (n == 2) {
                System.out.println("Please enter the First Name.");
                String f1 = scanner.nextLine();
                System.out.println("Please enter the Last Name.");
                String l1 = scanner.nextLine();
                System.out.println("Please enter the username.");
                String u1 = scanner.nextLine();
                System.out.println("Please enter the password.");
                String p1 = scanner.nextLine();
                sup.createManager(f1, l1, u1, p1);
                managerCreated = true;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        } while (!employeeCreated || !managerCreated);


        int input;

        while (true) {
            System.out.println("\nChoose a role:");
            System.out.println("1. server.domain.Employee");
            System.out.println("2. server.domain.Manager");
            System.out.println("3. server.domain.Supervisor");
            System.out.println("4. Exit");
            input = Integer.parseInt(scanner.nextLine());


            switch (input) {
                case 1:

                    System.out.println("Hey Employee!!");
                    System.out.println("Please Enter the Credentials:");


                    String u, p;
                    Employee authenticatedEmployee;
                    EmployeeService emp = new EmployeeServiceImpl();

                    do {
                        System.out.print("Username: ");
                        u = scanner.nextLine();
                        System.out.print("Password: ");
                        p = scanner.nextLine();

                        authenticatedEmployee = emp.findEmployee(u, p);

                        if (authenticatedEmployee != null) {
                            System.out.println("Credentials verified. Access granted!");
                            // Perform actions for authenticatedEmployee here
                            break;
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                    } while (true);

                    //working of employee here
                    emp.viewAssignedTasks(authenticatedEmployee);
                    emp.viewAllTasks();
                    break;
                case 2:
                    System.out.println("Hey Manager!!");
                    System.out.println("Please Enter the Credentials:");

                    // Variables to store username and password
                    String u1, p1;
                    Manager authenticatedManager;
                    ManagerService man = new ManagerServiceImpl();

                    do {
                        System.out.print("Username: ");
                        u1 = scanner.nextLine();
                        System.out.print("Password: ");
                        p1 = scanner.nextLine();

                        authenticatedManager = man.findManager(u1, p1);

                        if (authenticatedManager != null) {
                            System.out.println("Credentials verified. Access granted!");
                            // Perform actions for authenticatedEmployee here
                            break;
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                    } while (true);
                    //working of manager here
                    System.out.print("Please enter title for task to create.\n");
                    String t1 = scanner.nextLine();
                    man.createTask(authenticatedManager, t1, "Understand the situation and code it", 2);
                    System.out.print("Please enter title for task to create.\n");
                    String t2 = scanner.nextLine();
                    man.createTask(authenticatedManager, t2, "Debug the errors and remove it", 2);
                    man.assignTask();
                    //authenticatedManager.addComments("Improve work!!");
                    //authenticatedManager.viewallTasksbyEmp();
                    //authenticatedManager.viewallTasksbyStat();
                    //authenticatedManager.viewallTasks();
                    man.viewAllTasksByEmployeeAndStatus(authenticatedManager);
                    break;
                case 3:
                    System.out.println("Hey Supervisor!!");
                    System.out.println("Please Enter the Credentials:");

                    // Variables to store username and password
                    String u2;
                    String p2;
                    Supervisor supervisor = null;

                    // Loop until the credentials are verified
                    do {
                        System.out.print("Username: ");
                        u2 = scanner.nextLine();
                        System.out.print("Password: ");
                        p2 = scanner.nextLine();

                        supervisor = sup.verifyCredentials(u2, p2);

                        if (supervisor != null) {
                            System.out.println("Credentials verified. Access granted!");
                            // Perform actions for authenticatedEmployee here
                            break;
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                    } while (true);
                    // working of supervisor
                    break;
                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid input");
            }
        }


    }
}

