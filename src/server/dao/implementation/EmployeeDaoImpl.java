package server.dao.implementation;

import server.dao.EmployeeDao;
import server.domain.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {

    private static EmployeeDaoImpl instance;  // Singleton instance
    private List<Employee> employees = new ArrayList<>();

    // Private constructor to prevent external instantiation
    private EmployeeDaoImpl() {
    }

    // Method to get the Singleton instance
    public static EmployeeDaoImpl getInstance() {
        if (instance == null) {
            instance = new EmployeeDaoImpl();
        }
        return instance;
    }

    @Override
    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    @Override
    public void createEmployee(String firstname, String lastname, String username, String password) {
        Employee employee = new Employee(firstname, lastname, username, password);
        System.out.println("Employee created successfully.");
        addEmployee(employee);
    }

}
