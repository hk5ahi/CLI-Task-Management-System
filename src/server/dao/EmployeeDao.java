package server.dao;

import server.domain.Employee;

import java.util.List;

public interface EmployeeDao {
    List<Employee> getAllEmployees();

    Employee getEmployeeByUsername(String username);

    Employee createEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    boolean deleteEmployee(String username);
}
