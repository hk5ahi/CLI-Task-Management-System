package server.controller;

import org.springframework.web.bind.annotation.*;

import server.service.ManagerService;
import server.service.TaskService;
import server.utilities.*;


@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;
    private final UtilityService utilityService;

    private final TaskService taskService;

    public ManagerController(ManagerService managerService, UtilityService utilityService, TaskService taskService) {
        this.managerService = managerService;
        this.utilityService = utilityService;
        this.taskService = taskService;
    }







}
