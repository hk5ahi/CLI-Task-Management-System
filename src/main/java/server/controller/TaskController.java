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
            @RequestParam(name = "status",required = false,defaultValue = "false") boolean isStatus,
            @RequestParam(name = "employeeRole",required = false,defaultValue = "false") boolean isEmployeeRole,
            @RequestParam(name = "assigned",required = false,defaultValue = "false") boolean isAssigned,
            @RequestParam(name = "userRole",required = false,defaultValue = "N/A") User.UserRole userRole,
            @RequestParam(name = "managerRole",required = false,defaultValue = "false") boolean isManagerRole,
            @RequestParam(name = "task-status",required = false) Task.Status taskStatus,
            @RequestParam(name = "employeeName",required = false,defaultValue = "N/A") String employeeName,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        QueryParameterDTO queryParameterDTO = new QueryParameterDTO();
        queryParameterDTO.setStatus(isStatus);
        queryParameterDTO.setEmployeeRole(isEmployeeRole);
        queryParameterDTO.setAssigned(isAssigned);
        queryParameterDTO.setUserRole(userRole);
        queryParameterDTO.setManagerRole(isManagerRole);

        queryParameterDTO.setTaskStatus(taskStatus);
        queryParameterDTO.setEmployeeName(employeeName);

        List<TaskDTO> tasks = taskService.getTasksByController(queryParameterDTO, authorizationHeader);

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