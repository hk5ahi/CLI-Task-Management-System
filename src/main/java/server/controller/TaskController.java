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

        taskService.createTask(task, authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getTasks(
            @RequestParam(name = "status",required = false,defaultValue = "false") boolean byStatus,
            @RequestParam(name = "employeeRole",required = false,defaultValue = "false") boolean byEmployeeRole,
            @RequestParam(name = "assigned",required = false,defaultValue = "false") boolean byAssigned,
            @RequestParam(name = "userRole",required = false,defaultValue = "N/A") User.UserRole byUserRole,
            @RequestParam(name = "managerRole",required = false,defaultValue = "false") boolean byManagerRole,
            @RequestParam(name = "task-status",required = false) Task.Status byTaskStatus,
            @RequestParam(name = "employeeName",required = false,defaultValue = "N/A") String byEmployeeName,
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        QueryParameterDTO queryParameterDTO=QueryParameterDTO.createObjectFromQueryParameters(byStatus,byEmployeeRole,byAssigned,byUserRole,byManagerRole,byTaskStatus,byEmployeeName);
        List<TaskDTO> tasks = taskService.getTasks(queryParameterDTO, authorizationHeader);

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