package server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.dao.SupervisorDao;
import server.domain.*;

import server.dto.CommentDTO;
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
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UtilityService utilityService;
    private final SupervisorDao supervisorDao;


    public CommentController(CommentService commentService, UtilityService utilityService, SupervisorDao supervisorDao) {
        this.commentService = commentService;
        this.utilityService = utilityService;
        this.supervisorDao = supervisorDao;

    }

    @PostMapping("/add")
    public ResponseEntity<String> create(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CommentDTO comment
    ) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null) {
            if (authenticatedUserRole.equals(User.UserRole.Supervisor.toString())) {
                Optional<Supervisor> optionalSupervisor = supervisorDao.getSupervisorByName("Muhammad Asif");
                return optionalSupervisor.map(supervisor ->
                        commentService.addComments(comment.getMessage(), supervisor, comment.getTitle())
                ).orElse(null);

            } else if (authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
                Manager activeManager = utilityService.getActiveManager(authorizationHeader);
                return commentService.addComments(comment.getMessage(), activeManager, comment.getTitle());
            } else if (authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
                Employee activeEmployee = utilityService.getActiveEmployee(authorizationHeader);
                return commentService.addComments(comment.getMessage(), activeEmployee, comment.getTitle());
            } else {
                throw new ForbiddenAccessException();
            }
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @GetMapping("/view")
    public ResponseEntity<List<CommentDTO>> getComments(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("title") String title
    ) {
        Optional<String> authenticatedUserRole = Optional.ofNullable(utilityService.isAuthenticated(authorizationHeader));
        String supervisorRole = User.UserRole.Supervisor.toString();

        if (authenticatedUserRole.isPresent() && authenticatedUserRole.get().equals(supervisorRole)) {
            if (title == null) {
                return ResponseEntity.notFound().build();
            }
            List<CommentDTO> comments = commentService.viewComments(title);
            return ResponseEntity.ok(comments);
        } else {
            throw new ForbiddenAccessException();
        }
    }


}