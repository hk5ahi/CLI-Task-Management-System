//package client;
//
//import server.domain.*;
//import server.service.*;
//import server.service.Implementation.*;
//import server.utilities.Taskbytitle;
//
//import java.util.Scanner;
//
//
//public class CLI {
//    private static final int ROLE_EMPLOYEE = 1;
//    private static final int ROLE_MANAGER = 2;
//    private static final int ROLE_SUPERVISOR = 3;
//    private static final int EXIT = 4;
//
//    public static void main(String[] args) throws InterruptedException {
//        Scanner scanner = new Scanner(System.in);
//        EmployeeService employeeService = new EmployeeServiceImpl();
//        SupervisorService supervisorService = new SupervisorServiceImpl();
//        ManagerService managerService = new ManagerServiceImpl();
//        Employee employee = new Employee();
//        Manager manager = new Manager();
//        employeeService.initializeEmployee(employee);
//        managerService.initializeManager(manager);
//
//        while (true) {
//            System.out.println("\nChoose a role:");
//            System.out.println("1. Employee");
//            System.out.println("2. Manager");
//            System.out.println("3. Supervisor");
//            System.out.println("4. Exit");
//            int input = Integer.parseInt(scanner.nextLine());
//
//            switch (input) {
//                case ROLE_EMPLOYEE:
//                    handleEmployeeRole(scanner, employeeService);
//                    break;
//                case ROLE_MANAGER:
//                    handleManagerRole(scanner, managerService);
//                    break;
//                case ROLE_SUPERVISOR:
//                    handleSupervisorRole(scanner, supervisorService);
//                    break;
//                case EXIT:
//                    System.out.println("Exiting...");
//                    scanner.close();
//                    return;
//                default:
//                    System.out.println("Invalid input");
//            }
//        }
//    }
//
//    private static void handleEmployeeRole(Scanner scanner, EmployeeService employeeService) {
//        System.out.println("Hey Employee!!");
//        System.out.println("Please Enter the Credentials:");
//
//        String employee_username, employee_password;
//        Employee authenticatedEmployee;
//
//
//        do {
//            System.out.print("Username: ");
//            employee_username = scanner.nextLine();
//            System.out.print("Password: ");
//            employee_password = scanner.nextLine();
//
//            authenticatedEmployee = employeeService.findEmployee(employee_username, employee_password);
//
//            if (authenticatedEmployee != null) {
//                System.out.println("Credentials verified. Access granted!");
//                // Perform actions for authenticatedEmployee here
//                break;
//            } else {
//                System.out.println("Invalid credentials. Please try again.");
//            }
//        } while (true);
//
//
//        boolean exitMenu = false;
//
//        while (!exitMenu) {
//            System.out.println("Please select the task that you would like to do:");
//            System.out.println("1: Change task status");
//            System.out.println("2: Add total time before moving it to IN_PROGRESS to IN_REVIEW");
//            System.out.println("3: Add comments to a task");
//            System.out.println("4: View all assigned tasks");
//            System.out.println("5: View all tasks by status");
//            System.out.println("6: Return");
//
//            int input_employee = Integer.parseInt(scanner.nextLine());
//            TaskService taskService = new TaskServiceImpl();
//
//            switch (input_employee) {
//                case 1:
//
//                    Taskbytitle taskbytitle = new Taskbytitle();
//                    Task task = taskbytitle.getAssignTaskByTitle(authenticatedEmployee);
//
//                    if (task != null) {
//                        System.out.println("To which status do you want to change:");
//                        System.out.println("1:IN_PROGRESS");
//                        System.out.println("2:IN_REVIEW");
//
//                        int number = Integer.parseInt(scanner.nextLine());
//
//                        if (number == 1) {
//                            taskService.changeTaskStatus(task, Task.Status.valueOf("IN_PROGRESS"), authenticatedEmployee);
//
//                        } else if (number == 2) {
//                            taskService.changeTaskStatus(task, Task.Status.valueOf("IN_REVIEW"), authenticatedEmployee);
//
//                        } else {
//
//                            System.out.println("Invalid Input");
//                        }
//                    } else {
//
//                        System.out.println("There are no task to whose status can be changed.");
//                    }
//                    break;
//
//
//                case 2:
//
//                    System.out.println("Enter the minutes for task to be moved from IN Progress to IN Review.");
//                    double time = Double.parseDouble(scanner.nextLine());
//                    Taskbytitle assigntaskbytitle = new Taskbytitle();
//                    Task returntask = null;
//
//                    returntask = assigntaskbytitle.getAssignTaskByTitle(authenticatedEmployee);
//
//                    employeeService.addTotaltime(time, returntask);
//                    break;
//
//                case 3:
//                    Taskbytitle taskTitle = new Taskbytitle();
//                    Task task1 = taskTitle.gettaskbytitle();
//                    System.out.println("Please enter the comments for the task.");
//                    String message = scanner.nextLine();
//                    CommentService commentService = new CommentServiceImpl();
//                    commentService.addComments(message, authenticatedEmployee, task1);
//                    break;
//
//                case 4:
//
//                    taskService.viewAssignedTasks(authenticatedEmployee);
//                    break;
//
//                case 5:
//
//                    taskService.viewTasksByStatus(authenticatedEmployee);
//                    break;
//
//                case 6:
//                    exitMenu = true; // Set the flag to true to exit the loop
//                    break;
//
//                default:
//                    System.out.println("Invalid Input");
//                    break;
//            }
//        }
//
//
//    }
//
//    private static void handleManagerRole(Scanner scanner, ManagerService managerService) {
//        System.out.println("Hey Manager!!");
//        System.out.println("Please Enter the Credentials:");
//
//        // Variables to store username and password
//
//        String manager_username;
//        String manager_password;
//        Manager authenticatedManager;
//
//
//        do {
//            System.out.print("Username: ");
//            manager_username = scanner.nextLine();
//            System.out.print("Password: ");
//            manager_password = scanner.nextLine();
//            authenticatedManager = managerService.findManager(manager_username, manager_password);
//
//            if (authenticatedManager != null) {
//                System.out.println("Credentials verified. Access granted!");
//                // Perform actions for authenticatedEmployee here
//                break;
//            } else {
//                System.out.println("Invalid credentials. Please try again.");
//            }
//        } while (true);
//        //working of manager here
//
//
//        boolean exitMenuManager = false;
//
//        while (!exitMenuManager) {
//            System.out.println("Please select the task that you would like to do:");
//            System.out.println("1: Create Task");
//            System.out.println("2: Assign task to any employee");
//            System.out.println("3: Change task status");
//            System.out.println("4: Add comments to a task");
//            System.out.println("5: View all tasks created by YOU");
//            System.out.println("6: View all tasks by employee");
//            System.out.println("7: View all tasks by status");
//            System.out.println("8: View all tasks by employee and status");
//            System.out.println("9: Return");
//
//            int input_manager = Integer.parseInt(scanner.nextLine());
//            TaskServiceImpl taskService = new TaskServiceImpl();
//            Taskbytitle taskTitle = new Taskbytitle();
//
//            switch (input_manager) {
//                case 1:
//
//                    System.out.print("Please enter title for task to create.\n");
//                    String title = scanner.nextLine();
//
//                    System.out.print("Please enter description for task.\n");
//                    String description = scanner.nextLine();
//                    taskService.createTask(authenticatedManager, title, description, 1);
//
//                    break;
//
//
//                case 2:
//
//                    taskService.assignTask();
//                    break;
//
//                case 3:
//
//                    Task task = taskTitle.gettaskbytitle();
//
//                    System.out.println("To which status do you want to change:");
//                    System.out.println("1:COMPLETED");
//
//
//                    int number = Integer.parseInt(scanner.nextLine());
//
//                    if (number == 1) {
//                        taskService.changeTaskStatus(task, Task.Status.valueOf("COMPLETED"), authenticatedManager);
//
//                    } else {
//
//                        System.out.println("Invalid Input");
//                    }
//
//                    break;
//
//                case 4:
//
//                    Task returntask = taskTitle.gettaskbytitle();
//                    System.out.println("Please enter the comments for the task.");
//                    String message = scanner.nextLine();
//                    CommentService commentService = new CommentServiceImpl();
//                    commentService.addComments(message, authenticatedManager, returntask);
//                    break;
//
//                case 5:
//                    ManagerService managerService1 = new ManagerServiceImpl();
//                    managerService1.viewAllTasksCreatedByManager(authenticatedManager);
//                    break;
//
//                case 6:
//                    taskService.viewAllTasks();
//                    break;
//
//                case 7:
//                    taskService.viewAllTasksByStatusCreatedBySingleManager(authenticatedManager);
//                    break;
//
//                case 8:
//                    taskService.viewAllTasksByEmployeeAndStatusCreatedBySingleManager(authenticatedManager);
//                    break;
//                case 9:
//                    exitMenuManager = true; // Set the flag to true to exit the loop
//                    break;
//
//                default:
//                    System.out.println("Invalid Input");
//                    break;
//            }
//        }
//
//
//    }
//
//    private static void handleSupervisorRole(Scanner scanner, SupervisorService supervisorService) {
//        System.out.println("Hey Supervisor!!");
//        System.out.println("Please Enter the Credentials:");
//
//        // Variables to store username and password
//        String supervisor_username;
//        String supervisor_password;
//        Supervisor innersupervisor = null;
//
//        // Loop until the credentials are verified
//        do {
//            System.out.print("Username: ");
//            supervisor_username = scanner.nextLine();
//            System.out.print("Password: ");
//            supervisor_password = scanner.nextLine();
//
//            innersupervisor = supervisorService.verifyCredentials(supervisor_username, supervisor_password);
//
//            if (innersupervisor != null) {
//                System.out.println("Credentials verified. Access granted!");
//
//                break;
//            } else {
//                System.out.println("Invalid credentials. Please try again.");
//            }
//        } while (true);
//        // working of supervisor
//
//
//        boolean exitMenuSupervisor = false;
//
//        while (!exitMenuSupervisor) {
//            System.out.println("Please select the task that you would like to do:");
//            System.out.println("1: View all tasks");
//            System.out.println("2: View all tasks by status");
//            System.out.println("3: View all tasks by Employee with status");
//            System.out.println("4: View all tasks by Manager with status");
//            System.out.println("5: Archive Task");
//            System.out.println("6: Add comments to task");
//            System.out.println("7: View Task History");
//            System.out.println("8: View all Employees by User role");
//            System.out.println("9: Create User");
//
//            System.out.println("10: Return");
//
//            int input_supervisor = Integer.parseInt(scanner.nextLine());
//            TaskServiceImpl taskService = new TaskServiceImpl();
//            Taskbytitle taskTitle = new Taskbytitle();
//
//            switch (input_supervisor) {
//                case 1:
//
//                    taskService.viewAllTasks();
//                    break;
//
//                case 2:
//
//                    taskService.viewallTasksByStatus();
//                    break;
//
//                case 3:
//
//                    Employee anotheremployee = new Employee();
//                    anotheremployee.setUserRole("Employee");
//                    taskService.viewTasksByUser(anotheremployee);
//                    break;
//
//                case 4:
//
//                    Manager anothermanager = new Manager();
//                    anothermanager.setUserRole("Manager");
//                    taskService.viewTasksByUser(anothermanager);
//                    break;
//
//                case 5:
//                    taskService.archiveTask();
//                    break;
//
//                case 6:
//                    Task returntask = taskTitle.gettaskbytitle();
//                    System.out.println("Please enter the comments for the task.");
//                    String message = scanner.nextLine();
//                    CommentService commentService = new CommentServiceImpl();
//                    commentService.addComments(message, innersupervisor, returntask);
//                    break;
//
//                case 7:
//                    TaskHistoryService taskHistoryService = new TaskHistoryServiceImpl();
//                    taskHistoryService.viewTaskHistory();
//                    break;
//
//                case 8:
//                    System.out.println("1: To view Employees");
//                    System.out.println("2: To view Managers");
//
//                    int number = Integer.parseInt(scanner.nextLine());
//                    ManagerService managerService = new ManagerServiceImpl();
//                   // EmployeeService employeeService = new EmployeeServiceImpl(employeeDao);
//
//                    if (number == 1) {
//                        //employeeService.viewAllEmployees();
//
//                    } else if (number == 2) {
//                        managerService.viewAllManagers();
//                    } else {
//
//                        System.out.println("Invalid Input");
//                    }
//                    break;
//                case 9:
//
//                    System.out.println("1: To Create Employee");
//                    System.out.println("2: To Create Manager");
//                    UserService userService = null;
//                    int parseInt = Integer.parseInt(scanner.nextLine());
//
//                    if (parseInt == 1) {
//
//                        System.out.println("Please enter the First Name:");
//                        String firstName = scanner.nextLine();
//                        System.out.println("Please enter the Last Name:");
//                        String lastName = scanner.nextLine();
//                        System.out.println("Please enter the UserName:");
//                        String userName = scanner.nextLine();
//                        System.out.println("Please enter the Password:");
//                        String password = scanner.nextLine();
//                        userService.createUser("Employee", firstName, lastName, userName, password);
//
//                    } else if (parseInt == 2) {
//                        System.out.println("Please enter the First Name:");
//                        String firstName = scanner.nextLine();
//                        System.out.println("Please enter the Last Name:");
//                        String lastName = scanner.nextLine();
//                        System.out.println("Please enter the UserName:");
//                        String userName = scanner.nextLine();
//                        System.out.println("Please enter the Password:");
//                        String password = scanner.nextLine();
//                        userService.createUser("Manager", firstName, lastName, userName, password);
//
//                    } else {
//
//                        System.out.println("Invalid Input");
//                    }
//                    break;
//                case 10:
//                    exitMenuSupervisor = true; // Set the flag to true to exit the loop
//                    break;
//
//                default:
//                    System.out.println("Invalid Input");
//                    break;
//            }
//        }
//
//    }
//}
