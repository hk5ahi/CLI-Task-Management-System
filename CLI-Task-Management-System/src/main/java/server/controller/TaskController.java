package server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.dto.*;
import server.exception.ForbiddenAccessException;
import server.service.EmployeeService;
import server.service.ManagerService;
import server.service.TaskService;
import server.utilities.UtilityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final UtilityService utilityService;
    private final ManagerService managerService;
    private final EmployeeService employeeService;

    public TaskController(TaskService taskService, UtilityService utilityService, ManagerService managerService, EmployeeService employeeService) {
        this.taskService = taskService;
        this.utilityService = utilityService;
        this.managerService = managerService;
        this.employeeService = employeeService;
    }



    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody Task task, @RequestHeader("Authorization") String authorizationHeader) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Manager activeManager = managerService.findManager(username, password);
            if (activeManager != null) {
                taskService.createTask(activeManager, task.getTitle(), task.getDescription(), task.getTotal_time());
                return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully");
            } else {
                throw new ForbiddenAccessException(); // Manager not found or invalid credentials
            }
        }

        else {
            throw new ForbiddenAccessException();
        }
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignTask(@RequestHeader("Authorization") String authorizationHeader,@RequestBody AssignTaskRequestDTO request) {


        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {

            String response = taskService.assignTask(request.getTaskTitle(), request.getAssigneeName());
            return ResponseEntity.ok(response);

        } else {
            throw new ForbiddenAccessException();
        }
    }

    @GetMapping("/view-by-status")
    public ResponseEntity<List<TaskInfoDTO>> viewAllTasksByStatusCreatedBySingleManager(@RequestHeader("Authorization") String authorizationHeader) {

        //Manager
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Manager activeManager = managerService.findManager(username, password);
            List<TaskInfoDTO> tasksCreatedByManager = taskService.viewAllTasksByStatusCreatedBySingleManager(activeManager);
            return ResponseEntity.status(HttpStatus.OK).body(tasksCreatedByManager);

        } else {
            throw new ForbiddenAccessException();
        }
    }

    @GetMapping("/view-by-employee-and-status")
    public ResponseEntity<List<TaskbyEmployeeandStatusDTO>> viewTasksbyEmployeeandStatus(@RequestHeader("Authorization") String authorizationHeader) {

        //Manager
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Manager manager = managerService.findManager(username, password);


            List<TaskbyEmployeeandStatusDTO> taskbyEmployees=taskService.viewAllTasksByEmployeeAndStatusCreatedBySingleManager(manager);

            return ResponseEntity.status(HttpStatus.OK).body(taskbyEmployees);
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @GetMapping("/view-by-employee")
    public ResponseEntity<List<TaskbyEmployeeDTO>> viewTasksbyEmployee(@RequestHeader("Authorization") String authorizationHeader) {

        //Manager
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Manager manager = managerService.findManager(username, password);


            List<TaskbyEmployeeDTO> taskbyEmployees=taskService.viewAllTasks(manager);

            return ResponseEntity.status(HttpStatus.OK).body(taskbyEmployees);
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @GetMapping("/employee/view-assigned")
    public ResponseEntity<List<Task>> viewAssignedTasks(@RequestHeader("Authorization") String authorizationHeader) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Employee activeEmployee = employeeService.findEmployee(username, password);

            List<Task> assignedTasks = taskService.viewAssignedTasks(activeEmployee);

            return ResponseEntity.ok(assignedTasks);
        } else {
            throw new ForbiddenAccessException();
        }
    }


    @GetMapping("/employee/by-status")
    public ResponseEntity<List<TaskbyStatusDTO>> viewTasksbyStatus(@RequestHeader("Authorization") String authorizationHeader) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Employee activeEmployee = employeeService.findEmployee(username, password);

            List<TaskbyStatusDTO> Tasks = taskService.viewTasksByStatus(activeEmployee);

            return ResponseEntity.ok(Tasks);
        } else {
            throw new ForbiddenAccessException();
        }
    }

}
