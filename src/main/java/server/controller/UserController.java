package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.domain.User;
import server.dto.UserDTO;
import server.service.EmployeeService;
import server.service.ManagerService;
import server.service.SupervisorService;
import server.service.UserService;
import server.exception.ForbiddenAccessException;

import server.utilities.UtilityService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UtilityService utilityService;
    private final SupervisorService supervisorService;
    private final ManagerService managerService;
    private final EmployeeService employeeService;

    @Autowired
    public UserController(UserService userService, SupervisorService supervisorService, ManagerService managerService, EmployeeService employeeService,UtilityService utilityService) {
        this.userService = userService;
        this.supervisorService = supervisorService;
        this.managerService = managerService;
        this.employeeService = employeeService;
        this.utilityService=utilityService;
    }

    @GetMapping("/init")
    public String initUsers() {
        supervisorService.initializeSupervisor();
        managerService.initializeManager();
        employeeService.initializeEmployee();
        return "The users have been initialized successfully.";
    }

    @GetMapping("/view")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        // Check for authentication here. If user is not authenticated, return 401.
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);
        String value= String.valueOf(User.UserRole.Supervisor);
        if (authenticatedUserRole != null && authenticatedUserRole.equals(value)) {
            List<User> users = userService.allUsers();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } else  {
            throw new ForbiddenAccessException();
        }
    }



    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody User user, @RequestHeader("Authorization") String authorizationHeader) {
        // Check for authentication here. If user is not authenticated, return 401.
        Optional<String> authenticatedUserRole = Optional.ofNullable(utilityService.isAuthenticated(authorizationHeader));
        String supervisorRole = User.UserRole.Supervisor.toString();

        if (authenticatedUserRole.isPresent() && supervisorRole.equals(authenticatedUserRole.get())) {
            String status = userService.createUser(user.getUserRole(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword());
            if ("success".equals(status)) {
                // Return 201 Created when the resource is successfully created
                return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
            } else {
                // Return 400 Bad Request
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create user");
            }
        } else {
            throw new ForbiddenAccessException();
        }
    }


    @GetMapping("/view-all")
    public ResponseEntity<List<UserDTO>> viewAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        // Check for authentication here. If user is not authenticated, return 401.
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);
        String value= String.valueOf(User.UserRole.Supervisor);
        if (authenticatedUserRole != null && authenticatedUserRole.equals(value)) {

            return ResponseEntity.status(HttpStatus.OK).body(userService.viewallUsers());
        } else  {
            throw new ForbiddenAccessException();
        }
    }


}
