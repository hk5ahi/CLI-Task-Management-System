package server.service.Implementation;

import server.domain.Comment;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.service.ManagerService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

import static server.domain.Employee.*;
import static server.domain.Manager.getManagers;

public class ManagerServiceImpl implements ManagerService {


    @Override
    public Manager findManager(String providedUsername, String providedPassword) {
        for (Manager manager : getManagers()) {
            if (manager.getUsername().equals(providedUsername) && manager.getPassword().equals(providedPassword)) {
                return manager; // Return the matched Employee object
            }
        }
        return null; // If no match found, return null
    }

    @Override
    public void createTask(Manager activeManager, String title, String description, int total_time) {
        Task t = new Task(title, description, total_time);
        t.setCreatedBy(activeManager.getFirstName() + " " + activeManager.getLastName());

        t.setCreatedAt(LocalTime.now().toString());
        addTask(t);
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
            name11 = getEmployeeByName(name);

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
    public Task getTaskByTitle(String title) {
        for (Task task : getAllTasks()) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null; // Task not found
    }

    @Override
    public String getEmployeeByName(String name) {

        for (Manager manager : getManagers()) {
            String name0 = manager.getFirstName() + " " + manager.getLastName();
            if (name0.equalsIgnoreCase(name)) {

                return " ";
            }
        }

        for (Employee employee : getEmployees()) {
            String name0 = employee.getFirstName() + " " + employee.getLastName();
            if (name0.equalsIgnoreCase(name)) {
                return employee.getFirstName() + " " + employee.getLastName();
            }
        }
        return null; // Employee not found
    }

    @Override
    public void inReviewToCompleted(Manager activeManager) {

        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task whose status you want to change.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        if (task.getTaskStatus().equals("IN_REVIEW")) {
            task.setTaskStatus(String.valueOf(Task.Status.COMPLETED));
            task.getHistory().setTimestamp(LocalDateTime.now().toString());
            task.getHistory().setOldStatus("IN_REVIEW");
            task.getHistory().setNewStatus("COMPLETED");
            task.getHistory().setMovedBy(activeManager.getFirstName() + " " + activeManager.getLastName());


        } else {

            System.out.println("The task is not in desirable state.");
        }


    }

    @Override
    public void addComments(String message, Manager activeManager) {
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
        c.setCreatedBy(activeManager.getFirstName() + " " + activeManager.getLastName());
        task.addComment(c);
        System.out.printf("The comment has been added successfully by %s.\n", activeManager.getFirstName() + " " + activeManager.getLastName());
    }

    @Override
    public void viewAllTasks(Manager activeManager) {
        System.out.println("The tasks which are created by server.domain.Manager himself are: ");
        for (int i = 0; i < getAllTasks().size(); i++) {
            if (getAllTasks().get(i).getCreatedBy().equals(activeManager.getFirstName() + " " + activeManager.getLastName())) {
                System.out.printf("The title of task is %s with its description which is %s.\n", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription());
            }

        }


    }

    @Override
    public void viewAllTasksByEmployee(Manager activeManager) {

        for (int i = 0; i < getAllTasks().size(); i++) {
            if (getAllTasks().get(i).getCreatedBy().equals(activeManager.getFirstName() + " " + activeManager.getLastName())) {

                System.out.printf("The title of task is %s with its description which is %s and its employee is %s.\n", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription(), getAllTasks().get(i).getAssignee());
            }

        }
    }

    @Override
    public void viewAllTasksByStatus(Manager activeManager) {
        for (int i = 0; i < getAllTasks().size(); i++) {
            if (getAllTasks().get(i).getCreatedBy().equals(activeManager.getFirstName() + " " + activeManager.getLastName())) {

                System.out.printf("The title of task is %s with its description which is %s and its status is %s.\n", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription(), getAllTasks().get(i).getTaskStatus());
            }

        }
    }

    @Override
    public void viewAllTasksByEmployeeAndStatus(Manager activeManager) {
        for (int i = 0; i < getAllTasks().size(); i++) {
            if (getAllTasks().get(i).getCreatedBy().equals(activeManager.getFirstName() + " " + activeManager.getLastName())) {

                System.out.printf("The title of task is %s with its description which is %s whose employee is %s and  its status is %s.\n", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription(), getAllTasks().get(i).getAssignee(), getAllTasks().get(i).getTaskStatus());
            }

        }
    }
}
