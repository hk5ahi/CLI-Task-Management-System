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

    //POST comments is sufficed
    @PostMapping("/add")
    public ResponseEntity<String> create(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CommentDTO comment
    ) {
        //check how auth should be handled -> server.controller.UserController.getAllUsers
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);
        //check server.controller.UserController.getAllUsers to remove null
        if (authenticatedUserRole != null) {
            //you may add following logic in addComment method (till line 63) 
            //you may pass authenticated user in comment user and check its roles inside the service
            if (authenticatedUserRole.equals(User.UserRole.Supervisor.toString())) {
                //why hard coding supervisor name
                //always use username to get/fetch user
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
    
    //GET comments is sufficed

    @GetMapping("/view")
    public ResponseEntity<List<CommentDTO>> getComments(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("title") String title
    ) {
        //check how auth should be handled -> server.controller.UserController.getAllUsers
        Optional<String> authenticatedUserRole = Optional.ofNullable(utilityService.isAuthenticated(authorizationHeader));
        String supervisorRole = User.UserRole.Supervisor.toString();

        if (authenticatedUserRole.isPresent() && authenticatedUserRole.get().equals(supervisorRole)) {
            //avoid null
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