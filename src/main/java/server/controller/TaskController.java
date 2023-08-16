package server.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.Task;
import server.domain.User;
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

        taskService.createTaskByController(task, authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getTasks(
            @RequestParam(name = "status", defaultValue = "false") boolean status,
            @RequestParam(name = "employeeRole", defaultValue = "false") boolean employeeRole,
            @RequestParam(name = "assigned", defaultValue = "false") boolean assigned,
            @RequestParam(name = "userRole", defaultValue = "") User.UserRole userRole,
            @RequestParam(name = "manager", defaultValue = "false") boolean manager,
            @RequestParam(name = "no_criteria", defaultValue = "false") boolean noCriteria,
            @RequestParam(name = "task-status", defaultValue = "CREATED") Task.Status taskStatus,
            @RequestParam(name = "employeeName", defaultValue = " ") String employeeName,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        List<TaskDTO> tasks = taskService.getTasksByController(status, employeeRole, assigned, userRole,
                manager, noCriteria, taskStatus, employeeName,
                authorizationHeader);

        return ResponseEntity.ok(tasks);
    }



    @PutMapping()
    public ResponseEntity<String> update(

            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TaskDTO taskDTO
    ) {

        taskService.updateTasksByController(authorizationHeader, taskDTO);
        return ResponseEntity.status(HttpStatus.OK).build();


    }
}