package server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.Employee;

import server.domain.User;
import server.exception.ForbiddenAccessException;
import server.service.EmployeeService;
import server.service.TaskService;
import server.utilities.UtilityService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final UtilityService utilityService;

    public EmployeeController(EmployeeService employeeService, UtilityService utilityService) {
        this.employeeService = employeeService;
        this.utilityService = utilityService;

    }


    @PatchMapping("/time")
    public ResponseEntity<String> addTotalTime(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String title, @RequestParam double time) {

        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Employee activeEmployee = employeeService.findEmployee(username, password);

            return employeeService.addTotaltime(time, title,activeEmployee);


        } else {
            throw new ForbiddenAccessException();
        }
    }
}
