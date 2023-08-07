package server.dao.implementation;

import org.springframework.stereotype.Repository;
import server.dao.EmployeeDao;
import server.dao.UserDao;
import server.domain.Employee;
import server.domain.Supervisor;
import server.domain.User;

import java.util.ArrayList;
import java.util.List;
@Repository
public class EmployeeDaoImpl implements EmployeeDao {


    private List<Employee> employees = new ArrayList<>(List.of(new Employee("Muhammad", "Hanan", "m.hanan", "Ts12", "Employee")));

    @Override
    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    @Override
    public Employee createEmployee(String firstname, String lastname, String username, String password) {
        Employee employee = new Employee(firstname, lastname, username, password,"Employee");
        addEmployee(employee);
        return employee;

    }
    @Override
    public Employee findEmployee(String providedUsername, String providedPassword) {
        return employees.stream()
                .filter(employee -> employee.getUsername().equals(providedUsername) && employee.getPassword().equals(providedPassword))
                .findFirst()
                .orElse(null);
    }
    @Override
    public User getEmployeeByName(String name) {
        if(name!=null) {
            for (Employee employee : employees) {
                String employeeName = employee.getFirstName() + " " + employee.getLastName();
                if (employeeName.equalsIgnoreCase(name)) {

                    return employee;
                }
            }
        }
        return null; // Employee not found
    }


}
