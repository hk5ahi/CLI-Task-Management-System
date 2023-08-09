package server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.User;
import server.dto.TaskHistoryDTO;
import server.exception.ForbiddenAccessException;
import server.service.TaskHistoryService;
import server.utilities.UtilityService;

import java.util.Optional;

@RestController
@RequestMapping("/task-histories")//IMO, task-history is sufficed
public class TaskHistoryController {

    private final TaskHistoryService taskHistoryService;
    private final UtilityService utilityService;

    public TaskHistoryController(TaskHistoryService taskHistoryService, UtilityService utilityService) {
        this.taskHistoryService = taskHistoryService;
        this.utilityService = utilityService;
    }

    @GetMapping()
    public ResponseEntity<TaskHistoryDTO> getTaskHistory(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("title") String title) {

        //check how auth should be handled -> server.controller.UserController.getAllUsers
        Optional<String> authenticatedUserRole = Optional.ofNullable(utilityService.isAuthenticated(authorizationHeader));
        String supervisorRole = User.UserRole.Supervisor.toString();

        if (authenticatedUserRole.isPresent() && supervisorRole.equals(authenticatedUserRole.get())) {
            TaskHistoryDTO taskHistoryDTO = taskHistoryService.viewTaskHistory(title);
            return ResponseEntity.ok(taskHistoryDTO);
        } else {
            throw new ForbiddenAccessException();
        }
    }
}
