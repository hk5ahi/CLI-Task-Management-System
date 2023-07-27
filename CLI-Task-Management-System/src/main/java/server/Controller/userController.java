package server.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.service.Implementation.UserServiceImpl;

@RestController
@RequestMapping()
public class userController {

    private final UserServiceImpl userService;

    public userController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String printName() {
        return userService.printName();
    }

    @GetMapping("/error")
    public String handleError() {
        return "Error: Page not found.";
    }
}
