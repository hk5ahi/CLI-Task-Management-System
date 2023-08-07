package server.dao;

import server.domain.Employee;
import server.domain.User;

import java.util.List;

public interface EmployeeDao {


     List<Employee> getEmployees();

     void addEmployee(Employee employee);

     Employee createEmployee(String firstname, String lastname, String username, String password);

     User getEmployeeByName(String name);
     Employee findEmployee(String providedUsername, String providedPassword);

}
