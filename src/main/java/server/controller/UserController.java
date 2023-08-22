package server.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.User;
import server.dto.UserDTO;
import server.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }
    @PostMapping ("/init")
    public ResponseEntity<String> initialize() {
        userService.initializeUsers();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping()
    public ResponseEntity<String> create(
            @RequestBody User newUser,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        userService.create(newUser,authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers(authorizationHeader));
    }
}
