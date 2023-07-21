package server.service.Implementation;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.service.EmployeeService;
import server.service.TaskService;

import java.time.LocalTime;
import java.util.Scanner;

import static server.domain.Employee.*;
import static server.domain.Manager.getManagers;

public class TaskServiceImpl implements TaskService {

    EmployeeService employeeService = new EmployeeServiceImpl();

    @Override
    public void viewAllTasks() {

        for (int i = 0; i < getAllTasks().size(); i++) {

            System.out.printf("The title of task is %s with its description which is %s whose employee is %s and  its status is %s.It is created by %s", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription(), getAllTasks().get(i).getAssignee(), getAllTasks().get(i).getTaskStatus(), getAllTasks().get(i).getCreatedBy());


        }

    }

    @Override
    public void viewAllTasksByStatusCreatedBySingleManager(Manager activeManager) {
        for (int i = 0; i < getAllTasks().size(); i++) {
            if (getAllTasks().get(i).getCreatedBy().equals(activeManager.getFirstName() + " " + activeManager.getLastName())) {

                System.out.printf("The title of task is %s with its description which is %s and its status is %s.\n", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription(), getAllTasks().get(i).getTaskStatus());
            }

        }
    }

    @Override
    public void viewAllTasksByEmployeeAndStatusCreatedBySingleManager(Manager activeManager) {
        for (int i = 0; i < getAllTasks().size(); i++) {
            if (getAllTasks().get(i).getCreatedBy().equals(activeManager.getFirstName() + " " + activeManager.getLastName())) {

                System.out.printf("The title of task is %s with its description which is %s whose employee is %s and  its status is %s.\n", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription(), getAllTasks().get(i).getAssignee(), getAllTasks().get(i).getTaskStatus());
            }

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
    public void assignTask() {
        String title1;
        String name11 = null;
        Task task = null;
        Scanner scan = new Scanner(System.in);

        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
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

        title1 = task.getTitle();

        System.out.println("The Employees are:");
        for (Employee employee : getEmployees()) {
            System.out.println(employee.getFirstName() + " " + employee.getLastName());
        }

        // Loop until the correct name is entered and it's not the manager's name
        do {
            System.out.println("Enter the full name of the employee that you want to assign.");
            String name = scan.nextLine();
            name11 = employeeService.getEmployeeByName(name);

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

            if (task.isAssigned()) {
                System.out.println("Already assigned task.");
            } else {
                task.setAssignee(name11);
                task.setAssigned(true);
                System.out.printf("The Task titled %s is assigned to %s.\n", title1, name11);
                // Add the task to the assigned tasks list of the selected employee
                for (Employee employee : getEmployees()) {
                    String name = employee.getFirstName() + " " + employee.getLastName();
                    if (name.equalsIgnoreCase(name11)) {
                        employee.setAssignedTasks(task);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void viewTasksByEmployee() {
        System.out.println("The tasks are categorized employee-wise with their respective statuses.");

        for (int i = 0; i < getEmployees().size(); i++) {

            System.out.printf("The name of Employee is %s %s and its assigned tasks with their status are:\n", getEmployees().get(i).getFirstName(), getEmployees().get(i).getLastName());

            for (int j = 0; j < getAllTasks().size(); j++) {
                if (getAllTasks().get(j).getAssignee().equals(getEmployees().get(i).getFirstName() + " " + getEmployees().get(i).getLastName())) {

                    System.out.printf("The title of task is %s with its description which is %s and its status is %s and is created by %s.", getAllTasks().get(j).getTitle(), getAllTasks().get(j).getDescription(), getAllTasks().get(j).getTaskStatus(), getAllTasks().get(i).getCreatedBy());

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
    public void viewAssignedTasks(Employee employee) {
        System.out.print("The assigned tasks for the employee are:\n");
        for (int i = 0; i < employee.getAssignedTasks().size(); i++) {
            System.out.print(employee.getAssignedTasks().get(i).getDescription());
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
    public void createTask(Manager activeManager, String title, String description, int total_time) {
        Task t = new Task(title, description, total_time);
        t.setCreatedBy(activeManager.getFirstName() + " " + activeManager.getLastName());

        t.setCreatedAt(LocalTime.now().toString());
        addTask(t);
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


}
