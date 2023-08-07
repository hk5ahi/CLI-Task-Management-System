package server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.dao.EmployeeDao;
import server.domain.Employee;

import server.domain.User;
import server.exception.ForbiddenAccessException;
import server.service.EmployeeService;

import server.utilities.UtilityService;

import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeDao employeeDao;
    private final UtilityService utilityService;
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeDao employeeDao, UtilityService utilityService, EmployeeService employeeService) {
        this.employeeDao = employeeDao;

        this.utilityService = utilityService;

        this.employeeService = employeeService;
    }


    @PatchMapping("/task-time")
    public ResponseEntity<String> updateTotalTime(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String title,
            @RequestParam double time
    ) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Employee activeEmployee = employeeDao.findEmployee(username, password);

            return employeeService.addTotalTime(time, title, activeEmployee);
        } else {
            throw new ForbiddenAccessException();
        }
    }

}
