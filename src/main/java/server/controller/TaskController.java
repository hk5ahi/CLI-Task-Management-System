package server.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.dto.*;
import server.exception.ForbiddenAccessException;
import server.service.EmployeeService;
import server.service.TaskService;
import server.utilities.UtilityService;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UtilityService utilityService;
    private final ManagerDao managerDao;
    private final EmployeeDao employeeDao;

    public TaskController(TaskService taskService, UtilityService utilityService, ManagerDao managerDao, EmployeeDao employeeDao) {
        this.taskService = taskService;
        this.utilityService = utilityService;
        this.managerDao = managerDao;
        this.employeeDao = employeeDao;

    }


    @PostMapping()
    public ResponseEntity<String> createTask(@RequestBody TaskDTO task, @RequestHeader("Authorization") String authorizationHeader) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
            Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);

            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");

            Optional<Manager> optionalManager = managerDao.findManager(username, password);
            if (optionalManager.isPresent()) {
                Manager activeManager=optionalManager.get();
                return taskService.createTask(activeManager, task.getTitle(), task.getDescription(), task.getTotal_time());

            } else {
                throw new ForbiddenAccessException(); // Manager not found or invalid credentials
            }
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getTasks(
            @RequestParam(name = "status", defaultValue = "false") boolean status,
            @RequestParam(name = "employeeRole", defaultValue = "false") boolean employeeRole,
            @RequestParam(name = "assigned", defaultValue = "false") boolean assigned,
            @RequestParam(name = "userRole", defaultValue = "") String userRole,
            @RequestParam(name = "manager", defaultValue = "false") boolean manager,
            @RequestParam(name = "no_criteria", defaultValue = "false") boolean noCriteria,
            @RequestParam(name = "task-status", defaultValue = "CREATED") Task.Status task_status,
            @RequestParam(name = "employeeName", defaultValue = " ") String employeeName,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (authenticatedUserRole != null) {
            if (authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
                Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);
                String username = usernamePassword.get("username");
                String password = usernamePassword.get("password");
                Optional<Manager> optionalManager = managerDao.findManager(username, password);
                if(optionalManager.isPresent())
                {
                    Manager activeManager=optionalManager.get();
                    if (status && !employeeRole && User.UserRole.Manager.toString().equals(userRole) && !noCriteria && !assigned && manager) {
                        List<TaskDTO> tasksCreatedByManager = taskService.viewAllTasksCreatedByManager(activeManager,task_status);
                        return ResponseEntity.status(HttpStatus.OK).body(tasksCreatedByManager);
                    } else if (!status && employeeRole && User.UserRole.Manager.toString().equals(userRole) && !noCriteria && !assigned && manager && employeeName!=null) {
                        List<TaskDTO> tasksByEmployees = taskService.viewAllTasksCreatedByManager(activeManager,employeeName);
                        return ResponseEntity.status(HttpStatus.OK).body(tasksByEmployees);
                    } else if (status && employeeRole && User.UserRole.Manager.toString().equals(userRole) && !noCriteria && !assigned && manager) {
                        List<TaskDTO> tasksByEmployees = taskService.viewAllTasksCreatedByManager(activeManager,task_status,employeeName);
                        return ResponseEntity.status(HttpStatus.OK).body(tasksByEmployees);
                    } else {
                        throw new ForbiddenAccessException();
                    }
                }
                else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

                }


            } else if (authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
                Map<String, String> usernamePassword = utilityService.getUsernamePassword(authorizationHeader);
                String username = usernamePassword.get("username");
                String password = usernamePassword.get("password");
                Optional<Employee> optionalEmployee = employeeDao.findEmployee(username, password);
                if(optionalEmployee.isPresent())
                {
                    Employee activeEmployee=optionalEmployee.get();
                    if (assigned && !status && User.UserRole.Employee.toString().equals(userRole) && !manager && !employeeRole && !noCriteria) {
                        List<TaskDTO> assignedTasks = taskService.viewAssignedTasks(activeEmployee);
                        return ResponseEntity.ok(assignedTasks);
                    } else if (!assigned && status && User.UserRole.Employee.toString().equals(userRole) && !manager && !employeeRole && !noCriteria) {
                        List<TaskDTO> tasks = taskService.viewTasksByStatus(activeEmployee);
                        return ResponseEntity.ok(tasks);
                    } else {
                        throw new ForbiddenAccessException();
                    }
                }
                else {

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }


            } else if (authenticatedUserRole.equals(User.UserRole.Supervisor.toString())) {
                if (!assigned && noCriteria && !manager && !employeeRole && !status && User.UserRole.Supervisor.toString().equals(userRole)) {
                    return ResponseEntity.ok(taskService.viewAllTasksByUser(employeeName));
                } else if (!assigned && status && !manager && !employeeRole && User.UserRole.Supervisor.toString().equals(userRole) && !noCriteria) {
                    return ResponseEntity.status(HttpStatus.OK).body(taskService.viewAllTasksByStatus());
                } else if (!assigned && status && employeeRole && User.UserRole.Supervisor.toString().equals(userRole) && !noCriteria && !manager) {
                    return ResponseEntity.status(HttpStatus.OK).body(taskService.viewTasksByUser(User.UserRole.Employee.toString()));
                } else if (!assigned && status && manager && User.UserRole.Supervisor.toString().equals(userRole) && !noCriteria && !employeeRole) {
                    return ResponseEntity.status(HttpStatus.OK).body(taskService.viewTasksByUser(User.UserRole.Manager.toString()));
                } else {
                    throw new ForbiddenAccessException();
                }
            } else {
                throw new ForbiddenAccessException();
            }
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @PatchMapping()
    public ResponseEntity<String> update(
            @RequestParam(name = "status", defaultValue = "false") boolean status,
            @RequestParam(name = "archive", defaultValue = "false") boolean archive,
            @RequestParam(name = "assign", defaultValue = "false") boolean assign,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TaskDTO taskDTO
    ) {
        String authenticatedUserRole = utilityService.isAuthenticated(authorizationHeader);

        if (status && !archive && !assign) {
            if (authenticatedUserRole != null) {
                if (authenticatedUserRole.equals(User.UserRole.Employee.toString())) {
                    Employee activeEmployee = utilityService.getActiveEmployee(authorizationHeader);
                    return taskService.changeTaskStatus(taskDTO.getTitle(), taskDTO.getTaskStatus(), activeEmployee);
                } else if (authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
                    Manager manager = utilityService.getActiveManager(authorizationHeader);
                    return taskService.changeTaskStatus(taskDTO.getTitle(), taskDTO.getTaskStatus(), manager);
                }
            }
        } else if (!status && archive && !assign) {
            if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Supervisor.toString())) {
                return taskService.archiveTask(taskDTO.getTitle());
            }
        } else if (!status && !archive && assign) {
            if (authenticatedUserRole != null && authenticatedUserRole.equals(User.UserRole.Manager.toString())) {
                Manager activeManager = utilityService.getActiveManager(authorizationHeader);
                return taskService.assignTask(taskDTO.getTitle(), taskDTO.getAssignee(), activeManager);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }


}