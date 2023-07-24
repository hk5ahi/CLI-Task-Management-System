package server.service;

import server.domain.Employee;

public interface EmployeeService {


    void employeeCheck();

    void initializeEmployee(Employee employee);

    Employee findEmployee(String providedUsername, String providedPassword);

    String getEmployeeByName(String name);

    void viewAllEmployees();

    void createdToInProgress(Employee employee);

    void inProgressToInReview(Employee employee);


}
