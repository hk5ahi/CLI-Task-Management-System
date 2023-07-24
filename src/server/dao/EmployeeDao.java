package server.dao;

import server.domain.Employee;
import server.domain.Task;

import java.util.List;

public interface EmployeeDao {

     List<Task> getAssignedTasks(Employee employee);

     void addTask(Task allTasks);

     List<Task> getAllTasks();

     void setAssignedTasks(Task assignedTask, Employee employee);

     List<Employee> getEmployees();

     void setEmployees(Employee employee);

     void createEmployee(String firstname, String lastname, String username, String password);

}
