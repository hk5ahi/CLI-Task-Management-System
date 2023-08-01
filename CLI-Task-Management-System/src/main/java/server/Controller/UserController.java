package server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.domain.User;
import server.service.EmployeeService;
import server.service.ManagerService;
import server.service.SupervisorService;
import server.service.UserService;
import server.utilities.UnauthorizedAccessException;
import server.utilities.UserNotFoundException;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping()
public class UserController {

    private final UserService userService;
    private final SupervisorService supervisorService;
    private final ManagerService managerService;
    private final EmployeeService employeeService;

    @Autowired
    public UserController(UserService userService, SupervisorService supervisorService, ManagerService managerService, EmployeeService employeeService) {
        this.userService = userService;
        this.supervisorService = supervisorService;
        this.managerService = managerService;
        this.employeeService = employeeService;
    }

    @GetMapping("/user/init")
    public String initUsers() {
        supervisorService.initializeSupervisor();
        managerService.initializeManager();
        employeeService.initializeEmployee();
        return "The users have been initialized successfully.";
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        // Check for authentication here. If user is not authenticated, return 401.
        String authenticatedUser = isAuthenticated(authorizationHeader);
        if (authenticatedUser.equals("Supervisor")) {
            List<User> users = userService.allUsers();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } else if (authenticatedUser.equals("Manager") || authenticatedUser.equals("Employee")) {
            throw new UnauthorizedAccessException();
        } else {
            throw new UserNotFoundException();
        }
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody User user, @RequestHeader("Authorization") String authorizationHeader) {
        // Check for authentication here. If user is not authenticated, return 401.
        String authenticatedUser = isAuthenticated(authorizationHeader);
        if (authenticatedUser.equals("Not Found")) {
            throw new UserNotFoundException();
        } else if (authenticatedUser.equals("Supervisor")) {
            String status = userService.createUser(user.getUserRole(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword());
            if (status.equals("success")) {
                // Return 201 Created when the resource is successfully created
                return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
            } else {
                // Return 400 Bad Request
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create user");
            }
        } else {
            throw new UnauthorizedAccessException();
        }
    }

    // Method to check if the user is authenticated. Replace with your authentication logic.
    private String isAuthenticated(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            // Extract the Base64-encoded credentials from the header
            String encodedCredentials = authorizationHeader.substring("Basic ".length());

            // Decode the Base64-encoded credentials
            byte[] decodedCredentials = Base64.getDecoder().decode(encodedCredentials);
            String credentials = new String(decodedCredentials);

            // Extract the username and password from the credentials string
            String[] usernameAndPassword = credentials.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            // Now, you can perform the authentication based on the obtained username and password.
            // You may use the UserService or any other authentication mechanism here.
            // For example:
            return userService.verifyUser(username, password);
        }
        return null;
    }
}
