package server.service;

import server.domain.Employee;
import server.domain.Task;
import server.domain.User;

import java.util.List;

public interface EmployeeService {


    void initializeEmployee();

    String addTotaltime(double time, String title,Employee employee);

    Employee findEmployee(String providedUsername, String providedPassword);

    User getEmployeeByName(String name);

    List<Employee> getAllEmployees();


}
