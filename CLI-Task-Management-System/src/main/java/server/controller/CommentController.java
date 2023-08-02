package server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.dao.SupervisorDao;
import server.domain.*;

import server.dto.SentCommentDTO;
import server.dto.ViewCommentDTO;
import server.exception.ForbiddenAccessException;
import server.service.CommentService;
import server.service.EmployeeService;
import server.service.ManagerService;

import server.service.TaskService;
import server.utilities.UtilityService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    private final UtilityService utilityService;

    private final SupervisorDao supervisorDao;
    private final ManagerService managerService;
    private final EmployeeService employeeService;



    public CommentController(CommentService commentService, UtilityService utilityService, SupervisorDao supervisorDao, ManagerService managerService, EmployeeService employeeService, TaskService taskService) {
        this.commentService = commentService;
        this.utilityService = utilityService;
        this.supervisorDao = supervisorDao;

        this.managerService = managerService;
        this.employeeService = employeeService;

    }


    @PostMapping("/add")
    public ResponseEntity<String> addComments(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SentCommentDTO comment) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null) {
            String response;

            if (authenticatedUserRole.equals(User.UserRole.Supervisor.toString())) {
                Supervisor supervisor = supervisorDao.getSupervisorInfo();
                response = commentService.addComments(comment.getMessage(), supervisor, comment.getTitle());
            } else if (authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
                Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);
                String username = usernamePassword.get("username");
                String password = usernamePassword.get("password");
                Manager activeManager = managerService.findManager(username, password);
                response = commentService.addComments(comment.getMessage(), activeManager, comment.getTitle());
            } else if (authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
                Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);
                String username = usernamePassword.get("username");
                String password = usernamePassword.get("password");
                Employee activeEmployee = employeeService.findEmployee(username, password);
                response = commentService.addComments(comment.getMessage(), activeEmployee, comment.getTitle());
            } else {
                throw new ForbiddenAccessException();
            }

            return ResponseEntity.ok(response);
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @GetMapping("/view")
    public ResponseEntity<List<ViewCommentDTO>> viewComments( @RequestHeader("Authorization") String authorizationHeader,@RequestParam("title") String title) {

        Optional<String> authenticatedUserRole = Optional.ofNullable(utilityService.isAuthenticated(authorizationHeader));
        String supervisorRole = User.UserRole.Supervisor.toString();

        if (authenticatedUserRole.isPresent() && supervisorRole.equals(authenticatedUserRole.get())) {
            if (title == null) {
                return ResponseEntity.notFound().build();
            }
            List<ViewCommentDTO> comments = commentService.viewComments(title);
            return ResponseEntity.ok(comments);
        }
        else {
            throw new ForbiddenAccessException();
        }


    }



}



