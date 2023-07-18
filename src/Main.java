import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to the CLI Task Management System!!!");
        System.out.println("Press 1 if you are an Employee");
        System.out.println("Press 2 if you are a Manager");
        System.out.println("Press 3 if you are a Supervisor");

        Scanner scanner = new Scanner(System.in);
        int input = Integer.parseInt(scanner.nextLine());

        // Clear the console after taking user input
        clearConsole();

        // Process user input based on their role
        switch (input) {
            case 1:
                System.out.println("You are an Employee");
                break;
            case 2:
                System.out.println("You are a Manager");
                break;
            case 3:
                System.out.println("You are a Supervisor");
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
