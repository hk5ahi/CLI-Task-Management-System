package server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.User;
import server.exception.ForbiddenAccessException;
import server.service.TaskHistoryService;
import server.utilities.UtilityService;

import java.util.Optional;

@RestController
@RequestMapping("/task history")
public class TaskHistoryController {

    private final TaskHistoryService taskHistoryService;
    private final UtilityService utilityService;

    public TaskHistoryController(TaskHistoryService taskHistoryService, UtilityService utilityService) {
        this.taskHistoryService = taskHistoryService;
        this.utilityService = utilityService;
    }

    @PostMapping("/view")
    public ResponseEntity<String> archiveTask(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("title") String title) {

        Optional<String> authenticatedUserRole = Optional.ofNullable(utilityService.isAuthenticated(authorizationHeader));
        String supervisorRole = User.UserRole.Supervisor.toString();

        if (authenticatedUserRole.isPresent() && supervisorRole.equals(authenticatedUserRole.get())) {
            String result = taskHistoryService.viewTaskHistory(title);
            return ResponseEntity.ok(result);
        } else {
            throw new ForbiddenAccessException();
        }
    }
}
