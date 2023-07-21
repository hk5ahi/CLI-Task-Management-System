package server.service.Implementation;

import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.service.EmployeeService;
import server.service.TaskService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

import static server.domain.Employee.getAllTasks;
import static server.domain.Employee.getEmployees;
import static server.domain.Manager.getManagers;

public class EmployeeServiceImpl implements EmployeeService {


    @Override
    public void employeeCheck() {
        if (getEmployees().size() == 0) {
            System.out.println("Please first create Employee user from Supervisor.");
        }

    }

    @Override
    public Employee findEmployee(String providedUsername, String providedPassword) {

        if (getEmployees().size() != 0) {
            for (Employee employee : getEmployees()) {

                if (employee.getUsername().equals(providedUsername) && employee.getPassword().equals(providedPassword)) {
                    return employee; // Return the matched Employee object
                }
            }
            return null; // If no match found, return null
        }
        return null;
    }


    @Override
    public void createdToInProgress(Employee employee) {

        Task task = null;
        TaskService taskService = new TaskServiceImpl();
        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task whose status you want to change.");
            Scanner scan = new Scanner(System.in);
            String title = scan.nextLine();
            task = taskService.getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        if (task.getTaskStatus().equals("CREATED")) {

            task.setTaskStatus(String.valueOf(Task.Status.IN_PROGRESS));
            task.getHistory().setTimestamp(LocalDateTime.now().toString());
            task.setStartTime(LocalDateTime.now());
            task.getHistory().setOldStatus("CREATED");
            task.getHistory().setNewStatus("IN_PROGRESS");
            task.getHistory().setMovedBy(employee.getFirstName() + " " + employee.getLastName());

        } else {

            System.out.println("The task is not in desirable state.");
        }
    }

    @Override
    public void inProgressToInReview(Employee employee) {
        Task task = null;
        TaskService taskService = new TaskServiceImpl();
        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task whose status you want to change.");
            Scanner scan = new Scanner(System.in);
            String title = scan.nextLine();
            task = taskService.getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        if (task.getTaskStatus().equals("IN_PROGRESS")) {
            LocalDateTime endTime = LocalDateTime.now();
            Duration duration = Duration.between(task.getStartTime(), endTime);
            long minutes = duration.toMinutes();
            if (minutes >= task.getTotal_time()) {
                task.setTaskStatus(String.valueOf(Task.Status.IN_REVIEW));
                task.getHistory().setTimestamp(LocalDateTime.now().toString());
                task.getHistory().setOldStatus("IN_PROGRESS");
                task.getHistory().setNewStatus("IN_REVIEW");
                task.getHistory().setMovedBy(employee.getFirstName() + " " + employee.getLastName());

            } else {
                System.out.printf("The minimum time for task to stay in Progress State is %d minutes.", task.getTotal_time());
            }
        } else {
            System.out.println("The task is not in desirable state.");

        }
    }

    @Override
    public void createEmployee(String firstname, String lastname, String username, String password) {
        Employee e = new Employee(firstname, lastname, username, password);
        System.out.println("Employee created successfully.");

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
    public void viewAllEmployees() {
        System.out.println("The Employees of the System are:");

        for (int i = 0; i < getEmployees().size(); i++) {
            System.out.println(getEmployees().get(i).getFirstName() + " " + getEmployees().get(i).getLastName());

        }

    }


}



