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
                return taskService.createTask(activeManager, task.getTitle(), task.getDescription(), task.getTotal_time());

            } else {
                throw new ForbiddenAccessException(); // Manager not found or invalid credentials
            }
        } else {
            throw new ForbiddenAccessException();
        }
    }

    // PATCH tasks

    //GET tasks?status={}&employeeRole={}
    @GetMapping("/view")
    public ResponseEntity<List<TaskDTO>> getTasks(
            @RequestParam(name = "By status", defaultValue = "false") boolean status,
            @RequestParam(name = "By employee",defaultValue = "false") boolean employee,
            @RequestParam(name = "Assigned",defaultValue = "false") boolean assigned,
            @RequestParam(name = "userRole",defaultValue = " ") String userRole,
            @RequestParam(name = "By Manager",defaultValue = "false") boolean manager,
            @RequestParam(name = "No Criteria",defaultValue = "false") boolean no_criteria,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);
            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");
            Manager activeManager = managerService.findManager(username, password);

            if (status && !employee && userRole.equals(User.UserRole.Manager.toString())  && !no_criteria && !assigned && manager) {
                List<TaskDTO> tasksCreatedByManager = taskService.viewAllTasksByStatusCreatedBySingleManager(activeManager);
                return ResponseEntity.status(HttpStatus.OK).body(tasksCreatedByManager);
            } else if (!status && employee && userRole.equals(User.UserRole.Manager.toString())  && !no_criteria && !assigned && manager) {
                List<TaskDTO> taskbyEmployees = taskService.viewAllTasksbyUser(activeManager);
                return ResponseEntity.status(HttpStatus.OK).body(taskbyEmployees);
            } else if (status && employee && userRole.equals(User.UserRole.Manager.toString())  && !no_criteria && !assigned && manager) {
                List<TaskDTO> taskbyEmployees = taskService.viewAllTasksByEmployeeAndStatusCreatedBySingleManager(activeManager);
                return ResponseEntity.status(HttpStatus.OK).body(taskbyEmployees);
            } else {
                throw new ForbiddenAccessException();
            }
        } else if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);
            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");
            Employee activeEmployee = employeeService.findEmployee(username, password);

            if (assigned && !status && userRole.equals(User.UserRole.Employee.toString()) && !manager && !employee && !no_criteria) {
                List<TaskDTO> assignedTasks = taskService.viewAssignedTasks(activeEmployee);
                return ResponseEntity.ok(assignedTasks);
            } else if (!assigned && status && userRole.equals(User.UserRole.Employee.toString())&& !manager && !employee && !no_criteria) {
                List<TaskDTO> tasks = taskService.viewTasksByStatus(activeEmployee);
                return ResponseEntity.ok(tasks);
            } else {
                throw new ForbiddenAccessException();
            }
        } else if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Supervisor.toString())) {
            if (!assigned && no_criteria && !manager && !employee && !status && userRole.equals(User.UserRole.Supervisor.toString())) {
                return ResponseEntity.ok(taskService.viewAllTasksbyUser());
            } else if (!assigned && status && !manager && !employee && userRole.equals(User.UserRole.Supervisor.toString()) && !no_criteria) {
                return ResponseEntity.status(HttpStatus.OK).body(taskService.viewAllTasksByStatus());
            } else if (!assigned && status && employee && userRole.equals(User.UserRole.Supervisor.toString()) && !no_criteria && !manager) {
                return ResponseEntity.status(HttpStatus.OK).body(taskService.viewTasksByUser(User.UserRole.Employee.toString()));
            } else if (!assigned && status && manager && userRole.equals(User.UserRole.Supervisor.toString()) && !no_criteria && !employee) {
                return ResponseEntity.status(HttpStatus.OK).body(taskService.viewTasksByUser(User.UserRole.Manager.toString()));
            } else {
                throw new ForbiddenAccessException();
            }
        } else {
            throw new ForbiddenAccessException();
        }
    }

    //Update the task to change its status
    //PUT tasks (TaskDTO)
    @PatchMapping("update")
    public ResponseEntity<String> changeTaskDetails(
            @RequestParam(name = "status",defaultValue = "false") boolean status,
            @RequestParam(name = "archive",defaultValue = "false") boolean archive,
            @RequestParam(name = "assign",defaultValue = "false") boolean assign,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TaskDTO taskDTO
    ) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (status && !archive && !assign) {
            if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
                Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);
                String username = usernamePassword.get("username");
                String password = usernamePassword.get("password");
                Employee activeEmployee = employeeService.findEmployee(username, password);
                return taskService.changeTaskStatus(taskDTO.getTitle(), taskDTO.getTaskStatus(), activeEmployee);
            } else if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
                Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);
                String username = usernamePassword.get("username");
                String password = usernamePassword.get("password");
                Manager manager = managerService.findManager(username, password);
                return taskService.changeTaskStatus(taskDTO.getTitle(), taskDTO.getTaskStatus(), manager);
            } else {
                throw new ForbiddenAccessException();
            }
        } else if (!status && archive && !assign) {
            if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Supervisor.toString())) {
                return taskService.archiveTask(taskDTO.getTitle());

            } else {
                throw new ForbiddenAccessException();
            }
        } else if (!status && !archive && assign) {
            if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
                Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);
                String username = usernamePassword.get("username");
                String password = usernamePassword.get("password");
                Manager activeManager = managerService.findManager(username, password);
                return taskService.assignTask(taskDTO.getTitle(), taskDTO.getAssignee(), activeManager);
            } else {
                throw new ForbiddenAccessException();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}