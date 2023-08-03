package server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.Employee;
import server.domain.Manager;

import server.domain.User;
import server.dto.*;
import server.exception.ForbiddenAccessException;
import server.service.EmployeeService;
import server.service.ManagerService;
import server.service.TaskService;
import server.utilities.UtilityService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<String> createTask(@RequestBody createTaskDTO task, @RequestHeader("Authorization") String authorizationHeader) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Manager activeManager = managerService.findManager(username, password);
            if (activeManager != null) {
                String response=taskService.createTask(activeManager, task.getTitle(), task.getDescription(), task.getTotal_time());
                return  ResponseEntity.ok(response);
            } else {
                throw new ForbiddenAccessException(); // Manager not found or invalid credentials
            }
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignTask(@RequestHeader("Authorization") String authorizationHeader, @RequestBody AssignTaskRequestDTO request) {


        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {


            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Manager activeManager = managerService.findManager(username, password);

            String response = taskService.assignTask(request.getTaskTitle(), request.getAssigneeName(),activeManager);
            return ResponseEntity.ok(response);

        } else {
            throw new ForbiddenAccessException();
        }
    }

    @GetMapping("/manager/view-by-status")
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

    @GetMapping("/manager/view-by-employee-and-status")
    public ResponseEntity<List<TaskbyEmployeeandStatusDTO>> viewTasksbyEmployeeandStatus(@RequestHeader("Authorization") String authorizationHeader) {

        //Manager
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Manager manager = managerService.findManager(username, password);


            List<TaskbyEmployeeandStatusDTO> taskbyEmployees = taskService.viewAllTasksByEmployeeAndStatusCreatedBySingleManager(manager);

            return ResponseEntity.status(HttpStatus.OK).body(taskbyEmployees);
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @GetMapping("/manager/view-by-employee")
    public ResponseEntity<List<TaskbyEmployeeDTO>> viewTasksbyEmployee(@RequestHeader("Authorization") String authorizationHeader) {

        //Manager
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Manager manager = managerService.findManager(username, password);


            List<TaskbyEmployeeDTO> taskbyEmployees = taskService.viewAllTasksbyManager(manager);

            return ResponseEntity.status(HttpStatus.OK).body(taskbyEmployees);
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @GetMapping("/employee/view-assigned")
    public ResponseEntity<List<ViewAssignTask>> viewAssignedTasks(@RequestHeader("Authorization") String authorizationHeader) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Employee activeEmployee = employeeService.findEmployee(username, password);

            List<ViewAssignTask> assignedTasks = taskService.viewAssignedTasks(activeEmployee);

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

    @PostMapping("/change-status")
    public ResponseEntity<String> changeTaskStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TaskStatusDTO taskStatusDTO) {

        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Employee activeEmployee = employeeService.findEmployee(username, password);
            return ResponseEntity.ok(taskService.changeTaskStatus(taskStatusDTO.getTitle(), taskStatusDTO.getStatus(), activeEmployee));


        } else if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {

            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Manager manager = managerService.findManager(username, password);
            return ResponseEntity.ok(taskService.changeTaskStatus(taskStatusDTO.getTitle(), taskStatusDTO.getStatus(), manager));


        } else {

            throw new ForbiddenAccessException();
        }

    }

    @PostMapping("/archive")
    public ResponseEntity<String> archiveTask(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("title") String title) {

        Optional<String> authenticatedUserRole = Optional.ofNullable(utilityService.isAuthenticated(authorizationHeader));
        String supervisorRole = User.UserRole.Supervisor.toString();

        if (authenticatedUserRole.isPresent() && supervisorRole.equals(authenticatedUserRole.get())) {
            String result = taskService.archiveTask(title);
            return ResponseEntity.ok(result);
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @PostMapping("/supervisor/view-all")
    public ResponseEntity<List<TaskbyEmployeeDTO>> viewallTasksbySupervisor(@RequestHeader("Authorization") String authorizationHeader) {

        Optional<String> authenticatedUserRole = Optional.ofNullable(utilityService.isAuthenticated(authorizationHeader));
        String supervisorRole = User.UserRole.Supervisor.toString();

        if (authenticatedUserRole.isPresent() && supervisorRole.equals(authenticatedUserRole.get())) {

            return ResponseEntity.ok(taskService.viewAllTasksbySupervisor());
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @GetMapping("/supervisor/view-by-status")
    public ResponseEntity<List<TaskbyStatusDTO>> viewAllTasksByStatusbySupervisor(@RequestHeader("Authorization") String authorizationHeader) {


        Optional<String> authenticatedUserRole = Optional.ofNullable(utilityService.isAuthenticated(authorizationHeader));
        String supervisorRole = User.UserRole.Supervisor.toString();

        if (authenticatedUserRole.isPresent() && supervisorRole.equals(authenticatedUserRole.get())) {

            return ResponseEntity.status(HttpStatus.OK).body(taskService.viewallTasksByStatus());
        } else {
            throw new ForbiddenAccessException();
        }
     

        }

    @GetMapping("/supervisor/view-by-status-by-employee")
    public ResponseEntity<List<TaskbyEmployeeandStatusDTO>> viewAllTasksByStatusbyEmployee(@RequestHeader("Authorization") String authorizationHeader) {


        Optional<String> authenticatedUserRole = Optional.ofNullable(utilityService.isAuthenticated(authorizationHeader));
        String supervisorRole = User.UserRole.Supervisor.toString();

        if (authenticatedUserRole.isPresent() && supervisorRole.equals(authenticatedUserRole.get())) {

            return ResponseEntity.status(HttpStatus.OK).body(taskService.viewTasksByUser(User.UserRole.Employee.toString()));
        } else {
            throw new ForbiddenAccessException();
        }


    }

    @GetMapping("/supervisor/view-by-status-by-manager")
    public ResponseEntity<List<TaskbyEmployeeandStatusDTO>> viewAllTasksByStatusbyManager(@RequestHeader("Authorization") String authorizationHeader) {


        Optional<String> authenticatedUserRole = Optional.ofNullable(utilityService.isAuthenticated(authorizationHeader));
        String supervisorRole = User.UserRole.Supervisor.toString();

        if (authenticatedUserRole.isPresent() && supervisorRole.equals(authenticatedUserRole.get())) {

            return ResponseEntity.status(HttpStatus.OK).body(taskService.viewTasksByUser(User.UserRole.Manager.toString()));
        } else {
            throw new ForbiddenAccessException();
        }


    }

    }
    

