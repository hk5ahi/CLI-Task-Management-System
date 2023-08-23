package server.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.Task;
import server.dto.*;
import server.service.TaskService;
import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping()
    public ResponseEntity<String> createTask(@RequestBody TaskDTO task, @RequestHeader("Authorization") String authorizationHeader) {

        taskService.createTask(task, authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getTasks(
            @RequestParam(name = "task-status", required = false) Task.Status byTaskStatus,
            @RequestParam(name = "userName", required = false) String byUserName,
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        QueryParameterDTO queryParameterDTO = QueryParameterDTO.createObjectFromQueryParameters(byTaskStatus, byUserName);
        List<TaskDTO> tasks = taskService.getTasks(queryParameterDTO, authorizationHeader);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping()
    public ResponseEntity<String> update(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TaskDTO taskDTO) {
        taskService.updateTask(authorizationHeader, taskDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}