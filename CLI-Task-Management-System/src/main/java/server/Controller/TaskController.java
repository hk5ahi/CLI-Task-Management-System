package server.Controller;

import org.springframework.web.bind.annotation.RestController;
import server.service.TaskService;
@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
}
