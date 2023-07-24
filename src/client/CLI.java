package client;

import server.dao.TaskDao;
import server.dao.implementation.TaskDaoImpl;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Supervisor;
import server.service.EmployeeService;
import server.service.Implementation.EmployeeServiceImpl;
import server.service.Implementation.ManagerServiceImpl;
import server.service.Implementation.SupervisorServiceImpl;
import server.service.Implementation.TaskServiceImpl;
import server.service.ManagerService;
import server.service.SupervisorService;
import server.service.TaskService;

import java.util.Scanner;

public class CLI {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Employee employee = new Employee();
        Manager manager = new Manager();
        EmployeeService employeeService = new EmployeeServiceImpl();
        System.out.println("Welcome to the CLI Task Management System!!!");
        SupervisorService supervisorService = new SupervisorServiceImpl();
        ManagerService managerService = new ManagerServiceImpl();
        int input;

        while (true) {
            System.out.println("\nChoose a role:");
            System.out.println("1. Employee");
            System.out.println("2. Manager");
            System.out.println("3. Supervisor");
            System.out.println("4. Exit");
            input = Integer.parseInt(scanner.nextLine());


            switch (input) {
                case 1:

                    System.out.println("Hey Employee!!");
                    System.out.println("Please Enter the Credentials:");
                    employeeService.initializeEmployee(employee);

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
                    TaskService taskService = new TaskServiceImpl();
                    taskService.viewAssignedTasks(authenticatedEmployee);
                    taskService.viewAllTasks();
                    break;
                case 2:
                    System.out.println("Hey Manager!!");
                    System.out.println("Please Enter the Credentials:");
                    managerService.initializeManager(manager);
                    // Variables to store username and password

                    String manager_username;
                    String manager_password;
                    Manager authenticatedManager;


                    do {
                        System.out.print("Username: ");
                        manager_username = scanner.nextLine();
                        System.out.print("Password: ");
                        manager_password = scanner.nextLine();
                        authenticatedManager = managerService.findManager(manager_username, manager_password);

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
                    TaskDao anothertaskService = new TaskDaoImpl();
                    TaskService taskService1 = new TaskServiceImpl();
                    anothertaskService.createTask(authenticatedManager, t1, "Understand the situation and code it", 2);
                    System.out.print("Please enter title for task to create.\n");
                    String t2 = scanner.nextLine();
                    anothertaskService.createTask(authenticatedManager, t2, "Debug the errors and remove it", 2);
                    taskService1.assignTask();
                    //authenticatedManager.addComments("Improve work!!");
                    //authenticatedManager.viewallTasksbyEmp();
                    //authenticatedManager.viewallTasksbyStat();
                    //authenticatedManager.viewallTasks();
                    taskService1.viewAllTasksByEmployeeAndStatusCreatedBySingleManager(authenticatedManager);
                    break;
                case 3:
                    System.out.println("Hey Supervisor!!");
                    System.out.println("Please Enter the Credentials:");

                    // Variables to store username and password
                    String supervisor_username;
                    String supervisor_password;
                    Supervisor innersupervisor = null;

                    // Loop until the credentials are verified
                    do {
                        System.out.print("Username: ");
                        supervisor_username = scanner.nextLine();
                        System.out.print("Password: ");
                        supervisor_password = scanner.nextLine();

                        innersupervisor = supervisorService.verifyCredentials(supervisor_username, supervisor_password);

                        if (innersupervisor != null) {
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

