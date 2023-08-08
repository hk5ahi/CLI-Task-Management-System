package server.dao;

import server.domain.Employee;
import server.domain.User;

import java.util.List;
import java.util.Optional;

public interface EmployeeDao {


     List<Employee> getEmployees();

     void addEmployee(Employee employee);

     Employee createEmployee(String firstname, String lastname, String username, String password);

     Optional<Employee> getEmployeeByName(String name);
     Optional<Employee> findEmployee(String providedUsername, String providedPassword);

}
