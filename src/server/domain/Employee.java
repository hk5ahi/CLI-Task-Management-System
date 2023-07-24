package server.domain;
import java.util.ArrayList;
import java.util.List;

public class Employee extends User {

    private List<Task> assignedTasks = new ArrayList<>();
    private static List<Employee> employees = new ArrayList<>();
    private static List<Task> allTasks = new ArrayList<>();


    public Employee(String firstname, String lastname, String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstName(firstname);
        this.setLastName(lastname);
        employees.add(this);
    }

    public Employee() {

    }

    public List<Task> getAssignedTasks(Employee employee) {
        return employee.assignedTasks;
    }

    public static void addTask(Task allTasks) {
        Employee.allTasks.add(allTasks);
    }

    public static List<Task> getAllTasks() {
        return allTasks;
    }

    public void setAssignedTasks(Task assignedTask, Employee employee) {
        employee.assignedTasks.add(assignedTask);
    }

    public static List<Employee> getEmployees() {
        return employees;
    }

    public static void setEmployees(Employee employee) {
        Employee.employees.add(employee);
    }


}
