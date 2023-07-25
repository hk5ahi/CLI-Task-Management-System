package server.dao;

import server.domain.Employee;

import java.util.List;

public interface EmployeeDao {


     List<Employee> getEmployees();

     void addEmployee(Employee employee);

     void createEmployee(String firstname, String lastname, String username, String password);


}
