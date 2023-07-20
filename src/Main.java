import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the CLI Task Management System!!!");
        System.out.println("Welcome to Supervisor Login System");
        System.out.println("You have to create atleast one employee and manager user to access the employee and manager login system.");
        System.out.println("Hey Supervisor!!");
        System.out.println("Please Enter the Credentials:");

        // Variables to store username and password
        String u22;
        String p22;
        Supervisor sup = null;
        // Loop until the credentials are verified
        do {
            System.out.print("Username: ");
            u22 = scanner.nextLine();
            System.out.print("Password: ");
            p22 = scanner.nextLine();

            sup = Supervisor.verifyCredentials(u22, p22);

            if (sup != null) {
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
                Supervisor.createEmployee(f, l, u, p);
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
                Supervisor.createManager(f1, l1, u1, p1);
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
            System.out.println("3. Supervisor");
            System.out.println("4. Exit");
            input = Integer.parseInt(scanner.nextLine());


            switch (input) {
                case 1:

                    System.out.println("Hey Employee!!");
                    System.out.println("Please Enter the Credentials:");


                    String u, p;
                    Employee authenticatedEmployee;

                    do {
                        System.out.print("Username: ");
                        u = scanner.nextLine();
                        System.out.print("Password: ");
                        p = scanner.nextLine();

                        authenticatedEmployee = Employee.findEmployee(u, p);

                        if (authenticatedEmployee != null) {
                            System.out.println("Credentials verified. Access granted!");
                            // Perform actions for authenticatedEmployee here
                            break;
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                    } while (true);

                    //working of employee here
                    authenticatedEmployee.viewAssignedtasks();
                    authenticatedEmployee.allTasks();
                    break;
                case 2:
                    System.out.println("Hey Manager!!");
                    System.out.println("Please Enter the Credentials:");

                    // Variables to store username and password
                    String u1, p1;
                    Manager authenticatedManager;

                    do {
                        System.out.print("Username: ");
                        u1 = scanner.nextLine();
                        System.out.print("Password: ");
                        p1 = scanner.nextLine();

                        authenticatedManager = Manager.findManager(u1, p1);

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
                    authenticatedManager.createTask(t1, "Understand the situation and code it", 2);
                    System.out.print("Please enter title for task to create.\n");
                    String t2 = scanner.nextLine();
                    authenticatedManager.createTask(t2, "Debug the errors and remove it", 2);
                    authenticatedManager.assignTask();
                    //authenticatedManager.addComments("Improve work!!");
                    //authenticatedManager.viewallTasksbyEmp();
                    //authenticatedManager.viewallTasksbyStat();
                    //authenticatedManager.viewallTasks();
                    authenticatedManager.viewallTasksbyEmpandStat();
                    break;
                case 3:
                    System.out.println("Hey Supervisor!!");
                    System.out.println("Please Enter the Credentials:");

                    // Variables to store username and password
                    String u2;
                    String p2;
                    Supervisor sup1 = null;
                    // Loop until the credentials are verified
                do {
                    System.out.print("Username: ");
                    u2 = scanner.nextLine();
                    System.out.print("Password: ");
                    p2 = scanner.nextLine();

                    sup1 = Supervisor.verifyCredentials(u2, p2);

                    if (sup1 != null) {
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
