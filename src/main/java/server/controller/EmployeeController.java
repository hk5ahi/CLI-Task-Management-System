package server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.dao.EmployeeDao;
import server.domain.Employee;

import server.domain.User;
import server.exception.ForbiddenAccessException;
import server.service.EmployeeService;

import server.utilities.UtilityService;

import java.util.Map;
import java.util.Optional;

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
       Optional<User.UserRole> authenticatedUserRole = utilityService.getUserRole(authorizationHeader);

        if (authenticatedUserRole.isPresent() && authenticatedUserRole.get().equals(User.UserRole.Employee)) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Optional<Employee> optionalEmployee = employeeDao.findEmployee(username, password);
            return optionalEmployee.map(employee ->
                    employeeService.updateTotalTime(time, title, employee)
            ).orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
        } else {
            throw new ForbiddenAccessException();
        }
    }

}
