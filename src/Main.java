import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to the CLI Task Management System!!!");
        System.out.println("Press 1 if you are an Employee");
        System.out.println("Press 2 if you are a Manager");
        System.out.println("Press 3 if you are a Supervisor");

        Scanner scanner = new Scanner(System.in);
        int input = Integer.parseInt(scanner.nextLine());


        clearConsole();


        switch (input) {
            case 1:
                System.out.println("Hey Employee!!");
                System.out.println("Please Enter the Credentials:");


                String u;
                String p;


                do {
                    System.out.print("Username: ");
                    u = scanner.nextLine();
                    System.out.print("Password: ");
                    p = scanner.nextLine();


                    Employee e = new Employee();
                    boolean s = e.verifyCredentials(u, p);

                    if (s) {

                        System.out.println("Credentials verified. Access granted!");

                        break;
                    } else {

                        System.out.println("Invalid credentials. Please try again.");
                    }
                } while (true);
                Employee e = new Employee();
                //working of employee here
                break;
            case 2:
                System.out.println("Hey Manager!!");
                System.out.println("Please Enter the Credentials:");

                // Variables to store username and password
                String u1;
                String p1;

                // Loop until the credentials are verified
                do {
                    System.out.print("Username: ");
                    u1 = scanner.nextLine();
                    System.out.print("Password: ");
                    p1 = scanner.nextLine();

                    // Verify the credentials using the Employee class
                    Manager m = new Manager();
                    boolean s1 = m.verifyCredentials(u1, p1);

                    if (s1) {
                        // Do something when credentials are verified
                        System.out.println("Credentials verified. Access granted!");
                        break; // Exit the loop when the credentials are verified
                    } else {
                        // Do something when credentials are not verified
                        System.out.println("Invalid credentials. Please try again.");
                    }
                } while (true); // Keep looping until the credentials are verified

                break;
            case 3:
                System.out.println("Hey Supervisor!!");
                System.out.println("Please Enter the Credentials:");

                // Variables to store username and password
                String u2;
                String p2;

                // Loop until the credentials are verified
                do {
                    System.out.print("Username: ");
                    u2 = scanner.nextLine();
                    System.out.print("Password: ");
                    p2 = scanner.nextLine();

                    // Verify the credentials using the Employee class
                    Supervisor s = new Supervisor();
                    boolean s2 = s.verifyCredentials(u2, p2);

                    if (s2) {
                        // Do something when credentials are verified
                        System.out.println("Credentials verified. Access granted!");
                        break; // Exit the loop when the credentials are verified
                    } else {
                        // Do something when credentials are not verified
                        System.out.println("Invalid credentials. Please try again.");
                    }
                } while (true); // Keep looping until the credentials are verified

                break;
            default:
                System.out.println("Invalid input");
        }
    }

    public static void clearConsole() {
        for (int i = 0; i < 13; i++) {
            System.out.println();
        }
    }
}
