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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UtilityService utilityService;


    @Autowired
    public UserController(UserService userService,UtilityService utilityService) {
        this.userService = userService;
        this.utilityService=utilityService;
    }

    //here you can add
    //POST users/init
    @PostMapping ()
    public ResponseEntity<String> initialize() {
        userService.initializeUsers();
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    // POST users is sufficed
    // POST users/new is verbose
    @PostMapping("/new")
    public ResponseEntity<String> create(
            @RequestBody User newUser,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);
        String supervisorRole = User.UserRole.Supervisor.toString();
        //check how null is removed in server.controller.UserController.getAllUsers
        if (authenticatedUserRole != null && authenticatedUserRole.equals(supervisorRole)) {
            String creationStatus = userService.createUser(
                    newUser.getUserRole(),
                    newUser.getFirstName(),
                    newUser.getLastName(),
                    newUser.getUsername(),
                    newUser.getPassword()
            );
            if ("success".equals(creationStatus)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(null);
            } else {
                //exception should be handled at controller advice
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            //handled at utility service getAuthenticatedUser
            throw new ForbiddenAccessException();
        }
    }

    // GET users is sufficed
    // GET users/list is verbose

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        //this method will return Optional<AuthenticatedUser>
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);
        String supervisorRole = User.UserRole.Supervisor.toString();
        //this condition will become UserRole.Supervisor.equals(authenticationUser.getRole())
        if (authenticatedUserRole != null && authenticatedUserRole.equals(supervisorRole)) {
            List<UserDTO> userDTOS = userService.viewAllUsers();
            return ResponseEntity.status(HttpStatus.OK).body(userDTOS);
        } else {
            //utility service getAuthenticatedUser will handle this exception
            throw new ForbiddenAccessException();
        }
    }



}
