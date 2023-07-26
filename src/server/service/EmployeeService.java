package server.service;

import server.domain.Employee;
import server.domain.Task;
import server.domain.User;

public interface EmployeeService {


    void initializeEmployee(Employee employee);

    void addTotaltime(double time, Task task);

    Employee findEmployee(String providedUsername, String providedPassword);

    User getEmployeeByName(String name);

    void viewAllEmployees();


}
