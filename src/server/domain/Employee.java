package server.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Employee extends User {

    private List<Task> assignedTasks = new ArrayList<>();
    private static List<Employee> employees = new ArrayList<>();
    private static List<Task> allTasks = new ArrayList<>();
    private LocalDateTime startTime;

    public Employee(String firstname, String lastname, String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstName(firstname);
        this.setLastName(lastname);
        employees.add(this);
    }

    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public static void setAllTasks(List<Task> allTasks) {
        Employee.allTasks = allTasks;
    }

    public static List<Task> getAllTasks() {
        return allTasks;
    }

    public void setAssignedTasks(List<Task> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    public static List<Employee> getEmployees() {
        return employees;
    }

    public static void setEmployees(List<Employee> employees) {
        Employee.employees = employees;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

}
