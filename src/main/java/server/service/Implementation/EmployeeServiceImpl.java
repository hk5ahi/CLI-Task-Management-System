package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.TaskDao;
import server.dao.UserDao;
import server.domain.Employee;
import server.domain.Task;
import server.domain.User;
import server.exception.ForbiddenAccessException;
import server.service.EmployeeService;

import java.util.List;
@Service
public class EmployeeServiceImpl implements EmployeeService {

//    private EmployeeDaoImpl employeeDao = EmployeeDaoImpl.getInstance();

    private final EmployeeDao employeeDao;
    private final UserDao userDao;
    private final TaskDao taskDao;


    public EmployeeServiceImpl(EmployeeDao employeeDao, UserDao userDao, TaskDao taskDao) {
        this.employeeDao = employeeDao;
        this.userDao = userDao;

        this.taskDao = taskDao;
    }

    @Override
    public Employee findEmployee(String providedUsername, String providedPassword) {
        return employeeDao.getEmployees().stream()
                .filter(employee -> employee.getUsername().equals(providedUsername) && employee.getPassword().equals(providedPassword))
                .findFirst()
                .orElse(null);
    }



    @Override
    public void initializeEmployee()
    {
        Employee employee=new Employee();
        employee.setFirstName("Muhammad");
        employee.setLastName("Hanan");
        employee.setUsername("m.hanan");
        employee.setPassword("Ts12");
        employee.setUserRole("Employee");
        employeeDao.addEmployee(employee);
        userDao.addUser(employee);

    }

    @Override
    public String addTotaltime(double time, String title,Employee employee) {

        Task providedtask=null;
        if (title != null) {
            for (Task task : taskDao.getAllTasksbyEmployee()) {
                if (task != null && task.getTitle() != null && task.getTitle().equalsIgnoreCase(title)) {
                    providedtask=task;
                }
            }
        }

        if (providedtask == null) {
            return "There are no tasks to set total time.";

        }
        String assigneeName=providedtask.getAssignee().getFirstName()+" "+providedtask.getAssignee().getLastName();
        String employeeName=employee.getFirstName()+" "+employee.getLastName();
        if(assigneeName.equals(employeeName)) {
            providedtask.setTotal_time(time);
            return "The total time has been added successfully.";
        }
        else {

            throw new ForbiddenAccessException();
        }
    }


    @Override
    public User getEmployeeByName(String name) {
        if(name!=null) {
            for (Employee employee : employeeDao.getEmployees()) {
                String employeename = employee.getFirstName() + " " + employee.getLastName();
                if (employeename.equalsIgnoreCase(name)) {

                    return employee;
                }
            }
        }
        return null; // Employee not found
    }


    @Override
    public List<Employee> getAllEmployees() {
//        System.out.println("The Employees of the System are:");
//
//        for (Employee employee : employeeDao.getEmployees()) {
//            System.out.println(employee.getFirstName() + " " + employee.getLastName());
//        }

        return employeeDao.getEmployees();
    }


}



