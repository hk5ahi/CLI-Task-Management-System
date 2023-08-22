package server.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.dto.TaskHistoryDTO;
import server.service.TaskHistoryService;
import java.util.List;

@RestController
@RequestMapping("/task-history")
public class TaskHistoryController {
    private final TaskHistoryService taskHistoryService;
    public TaskHistoryController(TaskHistoryService taskHistoryService) {
        this.taskHistoryService = taskHistoryService;

    }
    @GetMapping()
    public ResponseEntity<List<TaskHistoryDTO>> getTaskHistory(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("title") String title) {

        return ResponseEntity.status(HttpStatus.OK).body(taskHistoryService.getTaskHistory(title,authorizationHeader));
    }
}
