package server.dao.implementation;
import server.dao.EmployeeDao;
import server.domain.Employee;
import server.domain.Task;

import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {
    @Override
    public List<Task> getAssignedTasks(Employee employee) {

        return employee.getAssignedTasks(employee);
    }

    @Override
    public void addTask(Task task) {
        Employee.getAllTasks().add(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return Employee.getAllTasks();
    }

    @Override
    public void setAssignedTasks(Task assignedTask, Employee employee) {
        employee.getAssignedTasks(employee).add(assignedTask);
    }

    @Override
    public List<Employee> getEmployees() {
        return Employee.getEmployees();
    }

    @Override
    public void setEmployees(Employee employee) {
        Employee.getEmployees().add(employee);
    }

    @Override
    public void createEmployee(String firstname, String lastname, String username, String password) {
        Employee e = new Employee(firstname, lastname, username, password);
        System.out.println("Employee created successfully.");
        setEmployees(e);

    }
}
