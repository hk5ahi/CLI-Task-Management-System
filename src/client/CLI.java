package client;

import server.domain.Employee;
import server.domain.Manager;
import server.domain.Supervisor;
import server.domain.Task;
import server.service.*;
import server.service.Implementation.*;
import server.utilities.Taskbytitle;

import java.util.Scanner;

public class CLI {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Employee employee = new Employee(); // for initialization
        Manager manager = new Manager();
        EmployeeService employeeService = new EmployeeServiceImpl();
        employeeService.initializeEmployee(employee);
        System.out.println("Welcome to the CLI Task Management System!!!");
        SupervisorService supervisorService = new SupervisorServiceImpl();
        ManagerService managerService = new ManagerServiceImpl();
        managerService.initializeManager(manager);
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


                    String employee_username, employee_password;
                    Employee authenticatedEmployee;


                    do {
                        System.out.print("Username: ");
                        employee_username = scanner.nextLine();
                        System.out.print("Password: ");
                        employee_password = scanner.nextLine();

                        authenticatedEmployee = employeeService.findEmployee(employee_username, employee_password);

                        if (authenticatedEmployee != null) {
                            System.out.println("Credentials verified. Access granted!");
                            // Perform actions for authenticatedEmployee here
                            break;
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                    } while (true);


                    boolean exitMenu = false;

                    while (!exitMenu) {
                        System.out.println("Please select the task that you would like to do:");
                        System.out.println("1: Change task status");
                        System.out.println("2: Add total time before moving it to IN_PROGRESS to IN_REVIEW");
                        System.out.println("3: Add comments to a task");
                        System.out.println("4: View all assigned tasks");
                        System.out.println("5: View all tasks by status");
                        System.out.println("6: Return");

                        int input_employee = Integer.parseInt(scanner.nextLine());

                        switch (input_employee) {
                            case 1:

                                Taskbytitle taskbytitle = new Taskbytitle();
                                Task task = taskbytitle.gettaskbytitle();

                                System.out.println("To which status do you want to change:");
                                System.out.println("1:IN_PROGRESS");
                                System.out.println("2:IN_REVIEW");

                                int number = Integer.parseInt(scanner.nextLine());
                                TaskService taskService = new TaskServiceImpl();
                                if (number == 1) {
                                    taskService.changeTaskStatus(task, Task.Status.valueOf("IN_PROGRESS"), authenticatedEmployee);

                                } else if (number == 2) {
                                    taskService.changeTaskStatus(task, Task.Status.valueOf("IN_REVIEW"), authenticatedEmployee);

                                } else {

                                    System.out.println("Invalid Input");
                                }

                                break;


                            case 2:
                                System.out.println("Enter the total time for task to be moved from IN Progress to IN Review.");
                                int time = Integer.parseInt(scanner.nextLine());
                                employeeService.addTotaltime(time);
                                break;

                            case 3:
                                Taskbytitle taskTitle = new Taskbytitle();
                                Task task1 = taskTitle.gettaskbytitle();
                                System.out.println("Please enter the comments for the task.");
                                String message = scanner.nextLine();
                                CommentService commentService = new CommentServiceImpl();
                                commentService.addComments(message, authenticatedEmployee, task1);
                                break;

                            case 4:
                                TaskService taskService1 = new TaskServiceImpl();
                                taskService1.viewAssignedTasks(authenticatedEmployee);
                                break;

                            case 5:
                                TaskService taskService11 = new TaskServiceImpl();
                                taskService11.viewTasksByStatus();
                                break;

                            case 6:
                                exitMenu = true; // Set the flag to true to exit the loop
                                break;

                            default:
                                System.out.println("Invalid Input");
                                break;
                        }
                    }

                    break;
                case 2:
                    System.out.println("Hey Manager!!");
                    System.out.println("Please Enter the Credentials:");

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

                    System.out.println("Please select the task that you would like to do:");
                    System.out.println("1: Create Task");
                    System.out.println("2: Assign task to any employee");
                    System.out.println("3: Change task status from IN_REVIEW to COMPLETED");
                    System.out.println("4: Add comments to a task");
                    System.out.println("5: View all tasks created by YOU");
                    System.out.println("6: View all tasks by employee");
                    System.out.println("7: View all tasks by status");
                    System.out.println("8: View all tasks by employee and status");


                    System.out.print("Please enter title for task to create.\n");
                    String t1 = scanner.nextLine();
                    TaskServiceImpl taskService = new TaskServiceImpl();

                    taskService.createTask(authenticatedManager, t1, "Understand the situation and code it", 2);
                    System.out.print("Please enter title for task to create.\n");
                    String t2 = scanner.nextLine();
                    taskService.createTask(authenticatedManager, t2, "Debug the errors and remove it", 2);
                    taskService.assignTask();
                    //authenticatedManager.addComments("Improve work!!");
                    //authenticatedManager.viewallTasksbyEmp();
                    //authenticatedManager.viewallTasksbyStat();
                    //authenticatedManager.viewallTasks();
                    taskService.viewAllTasksByEmployeeAndStatusCreatedBySingleManager(authenticatedManager);
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


                    System.out.println("Please select the task that you would like to do:");
                    System.out.println("1: View all tasks");
                    System.out.println("2: View all tasks by status");
                    System.out.println("3: View all tasks by Employee with status");
                    System.out.println("4: View all tasks by Manager with status");
                    System.out.println("5: Archive Task");
                    System.out.println("6: Add comments to task");
                    System.out.println("7: View Task History");
                    System.out.println("8: View all Employees by User role");
                    System.out.println("9: Create Employee User");
                    System.out.println("10: Create Manager User");


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

