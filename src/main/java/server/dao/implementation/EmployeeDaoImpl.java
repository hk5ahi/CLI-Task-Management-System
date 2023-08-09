package server.dao.implementation;

import org.springframework.stereotype.Repository;
import server.dao.EmployeeDao;
import server.dao.UserDao;
import server.domain.Employee;
import server.domain.Supervisor;
import server.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {


    private List<Employee> employees = new ArrayList<>(List.of(new Employee("Muhammad", "Hanan", "m.hanan", "Ts12", User.UserRole.Employee)));

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
        Employee employee = new Employee(firstname, lastname, username, password,User.UserRole.Employee);
        addEmployee(employee);
        return employee;

    }
    @Override
    public Optional<Employee> findEmployee(String providedUsername, String providedPassword) {
        return employees.stream()
                .filter(employee -> employee.getUsername().equals(providedUsername) && employee.getPassword().equals(providedPassword))
                .findFirst();
    }

    @Override
    public Optional<Employee> getEmployeeByName(String name) {
        if(name!=null) {
            for (Employee employee : employees) {
                String employeeName = employee.getFirstName() + " " + employee.getLastName();
                if (employeeName.equalsIgnoreCase(name)) { // can't search on basis of username as full name will be passed in input

                    return Optional.of(employee);
                }
            }
        }
        return Optional.empty(); // Employee not found
    }


}
