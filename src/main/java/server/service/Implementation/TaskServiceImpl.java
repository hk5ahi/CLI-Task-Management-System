package server.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.dao.*;

import server.domain.*;
import server.dto.*;
import server.exception.*;
import server.service.EmployeeService;
import server.service.TaskService;
import server.utilities.UtilityService;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskDao taskDao;
    private final EmployeeDao employeeDao;
    private final UserDao userDao;
    private final TaskHistoryDao taskHistoryDao;
    private final UtilityService utilityService;
    private final EmployeeService employeeService;


    @Autowired
    public TaskServiceImpl(TaskDao taskDao, EmployeeDao employeeDao, UserDao userDao, TaskHistoryDao taskHistoryDao, UtilityService utilityService, EmployeeService employeeService) {

        this.taskDao = taskDao;
        this.employeeDao = employeeDao;
        this.userDao = userDao;

        this.taskHistoryDao = taskHistoryDao;
        this.utilityService = utilityService;
        this.employeeService = employeeService;
    }


    @Override
    public List<TaskDTO> getAllTasksCreatedByManager(Manager manager, String employeeName) {
        String[] nameParts = employeeName.split(" ");

        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        Optional<Employee> optionalEmployee = employeeDao.getEmployeeByFirstNameAndLastName(firstName,lastName);

        if (optionalEmployee.isEmpty()) {
            return Collections.emptyList();
        }

        Employee employee = optionalEmployee.get();
        List<TaskDTO> taskByEmployees = new ArrayList<>();
        List<Task> tasks = taskDao.getTasksByCreatedByUsernameAndAssignee_Username(manager.getUsername(), employee.getUsername());

        for (Task task : tasks) {
            TaskDTO taskByEmployee = createTaskDTOFromTask(task);
            taskByEmployees.add(taskByEmployee);
        }

        return taskByEmployees;
    }

    private TaskDTO createTaskDTOFromTask(Task task) {
        String creatorName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();
        String assigneeName = "N/A"; // Default value in case assignee is null
        if (task.getAssignee() != null) {
            assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
        }
        Instant timestamp = task.getCreatedAt();

        TaskDTO taskByEmployee = new TaskDTO();
        taskByEmployee.setTitle(task.getTitle());
        taskByEmployee.setDescription(task.getDescription());
        taskByEmployee.setAssignee(assigneeName);
        taskByEmployee.setCreatedAt(timestamp);
        taskByEmployee.setCreatedBy(creatorName);

        return taskByEmployee;
    }


    @Override
    public List<TaskDTO> getAllTasksByUser(String employeeName) {
        String[] nameParts = employeeName.split(" ");

        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        Optional<Employee> optionalEmployee = employeeDao.getEmployeeByFirstNameAndLastName(firstName,lastName);

        if (optionalEmployee.isEmpty()) {
            return Collections.emptyList();
        }

        Employee employee = optionalEmployee.get();
        List<TaskDTO> taskByEmployees = new ArrayList<>();
        List<Task> tasks = taskDao.getTasksByAssignee_Username(employee.getUsername());

        for (Task task : tasks) {
            TaskDTO taskByEmployee = createTaskDTOFromTaskByUser(task);
            taskByEmployees.add(taskByEmployee);
        }

        return taskByEmployees;
    }

    private TaskDTO createTaskDTOFromTaskByUser(Task task) {
        String assigneeName = task.getAssignee() != null ? task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName() : "N/A";
        String creatorName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();
        Instant timestamp = task.getCreatedAt();

        TaskDTO taskByEmployee = new TaskDTO();
        taskByEmployee.setTitle(task.getTitle());
        taskByEmployee.setDescription(task.getDescription());
        taskByEmployee.setAssignee(assigneeName);
        taskByEmployee.setCreatedAt(timestamp);
        taskByEmployee.setCreatedBy(creatorName);
        taskByEmployee.setTaskStatus(task.getTaskStatus());
        taskByEmployee.setTotal_time(task.getTotal_time());

        return taskByEmployee;
    }

    @Override
    public void createTask(Manager activeManager, TaskDTO taskDTO) {

        Task task = new Task(taskDTO.getTitle(), taskDTO.getDescription(), taskDTO.getTotal_time());
        task.setCreatedBy(activeManager);
        Instant currentInstant = Instant.now();
        task.setCreatedAt(currentInstant);
        if (taskDao.existsByTitle(task.getTitle())) {

           throw new BadRequestException();
        } else {
            taskDao.save(task);

        }
    }
    @Override
    public List<TaskDTO> getTasksByController(QueryParameterDTO queryParameterDTO, String header) {

        if (utilityService.isAuthenticatedSupervisor(header) && User.UserRole.Supervisor.equals(queryParameterDTO.getUserRole())) {
            return handleSupervisorTasks(queryParameterDTO.isStatus(), queryParameterDTO.isAssigned(),queryParameterDTO.isEmployeeRole(), queryParameterDTO.isNoCriteria(),queryParameterDTO.isManagerRole(), queryParameterDTO.getEmployeeName());
        } else if (utilityService.isAuthenticatedManager(header) && User.UserRole.Manager.equals(queryParameterDTO.getUserRole())) {
            return handleManagerTasks(queryParameterDTO.isStatus(), queryParameterDTO.isAssigned(),queryParameterDTO.isEmployeeRole(),queryParameterDTO.isNoCriteria(),queryParameterDTO.isManagerRole(), queryParameterDTO.getEmployeeName(),queryParameterDTO.getTaskStatus(),header);
        } else if (utilityService.isAuthenticatedEmployee(header) && User.UserRole.Employee.equals(queryParameterDTO.getUserRole())) {
            return handleEmployeeTasks(queryParameterDTO.isAssigned(), queryParameterDTO.isStatus(), queryParameterDTO.isEmployeeRole(),queryParameterDTO.isNoCriteria(),header);
        }

        throw new ForbiddenAccessException();
    }

    private void validateGetAllTasksByUser()
    {


    }

    private List<TaskDTO> handleSupervisorTasks(boolean status, boolean assigned, boolean employeeRole,
                                                          boolean noCriteria, boolean manager, String employeeName) {
        if (!assigned && noCriteria && !manager && !employeeRole && !status) {
            return getAllTasksByUser(employeeName);
        } else if (!assigned && status && !manager && !employeeRole && !noCriteria) {
            return getAllTasksByStatus();
        } else if (!assigned && status && employeeRole && !noCriteria && !manager) {
            return getTasksByUser(User.UserRole.Employee);
        } else if (!assigned && status && manager && !noCriteria && !employeeRole) {
            return getTasksByUser(User.UserRole.Manager);
        }
        return Collections.emptyList();
    }

    private List<TaskDTO> handleManagerTasks(boolean status, boolean assigned, boolean employeeRole,
                                                       boolean noCriteria, boolean manager, String employeeName,
                                                       Task.Status taskStatus,String header) {
        Manager activeManager = utilityService.getActiveManager(header)
                .orElseThrow(BadRequestException::new);

        if (status && !employeeRole && !noCriteria && !assigned && manager) {
            return getAllTasksCreatedByManager(activeManager, taskStatus);
        } else if (!status && employeeRole && !noCriteria && !assigned && manager && employeeName != null) {
            return getAllTasksCreatedByManager(activeManager, employeeName);
        } else if (status && employeeRole && !noCriteria && !assigned && manager) {
            return getAllTasksCreatedByManager(activeManager, taskStatus, employeeName);
        }
        return Collections.emptyList();
    }

    private List<TaskDTO> handleEmployeeTasks(boolean assigned, boolean status, boolean employeeRole,
                                                        boolean noCriteria,String header) {
        Employee activeEmployee = utilityService.getActiveEmployee(header)
                .orElseThrow(BadRequestException::new);

        if (assigned && !status && !employeeRole && !noCriteria) {
            return getAssignedTasks(activeEmployee);
        } else if (!assigned && status && !employeeRole && !noCriteria) {
            return getTasksByStatus(activeEmployee);
        }
        return Collections.emptyList();
    }

    @Override
    public void updateTasksByController(String authorizationHeader, TaskDTO taskDTO) {

        validateTaskArchive(authorizationHeader,taskDTO);
        validateTaskStatus(authorizationHeader,taskDTO);
        validateTaskAssign(authorizationHeader,taskDTO);
        validateUpdateTime(authorizationHeader,taskDTO);
    }


    private void validateTaskAssign(String authorizationHeader, TaskDTO taskDTO)
    {
        if (utilityService.isAuthenticatedManager(authorizationHeader)) {
            Manager activeManager = utilityService.getActiveManager(authorizationHeader)
                    .orElseThrow(BadRequestException::new);
            assignTask(taskDTO, activeManager);
        }
        else {

            throw new ForbiddenAccessException();
        }


    }
    private void validateTaskArchive(String authorizationHeader, TaskDTO taskDTO) {
        if (utilityService.isAuthenticatedSupervisor(authorizationHeader)) {

            archiveTask(taskDTO);
        } else {
            throw new ForbiddenAccessException();
        }
    }

    private void validateTaskStatus(String authorizationHeader, TaskDTO taskDTO) {
        if (utilityService.isAuthenticatedSupervisor(authorizationHeader)) {
            throw new ForbiddenAccessException();
        } else {
            Optional<User> optionalUser = validateAndGetUser(authorizationHeader);
            if (optionalUser.isPresent()) {
                changeTaskStatus(taskDTO, optionalUser.get());
            } else {
                throw new BadRequestException();
            }
        }
    }


    private Optional<User> validateAndGetUser(String authorizationHeader) {
        if (utilityService.isAuthenticatedManager(authorizationHeader)) {
            return Optional.of(utilityService.getActiveManager(authorizationHeader)
                    .orElseThrow(BadRequestException::new));
        } else if (utilityService.isAuthenticatedEmployee(authorizationHeader)) {
            return Optional.of(utilityService.getActiveEmployee(authorizationHeader)
                    .orElseThrow(BadRequestException::new));
        } else {
            throw new BadRequestException();
        }
    }
    private void validateUpdateTime(String header,TaskDTO taskDTO)
    {
        if (utilityService.isAuthenticatedEmployee(header)) {
            Employee activeEmployee = utilityService.getActiveEmployee(header)
                    .orElseThrow(BadRequestException::new);
            employeeService.updateTotalTime(taskDTO,activeEmployee);
        }
        else {

            throw new ForbiddenAccessException();
        }

    }

    @Override
    public void createTaskByController(TaskDTO task, String header) {
        if (utilityService.isAuthenticatedManager(header)) {
            Manager activeManager = utilityService.getActiveManager(header)
                    .orElseThrow(ForbiddenAccessException::new);

            createTask(activeManager, task);
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @Override
    public void changeTaskStatus(TaskDTO taskDTO, User person) {
        Task task = getValidatedTask(taskDTO.getTitle());

            Task.Status currentStatus = task.getTaskStatus();
            if(currentStatus.equals(taskDTO.getTaskStatus()))
            {
                throw new BadRequestException();
            }
            String createdUserName = task.getCreatedBy().getUsername();

            boolean isValidChange = switch (person.getUserRole()) {
                case Employee -> handleEmployeeChangeStatus(taskDTO.getTaskStatus(), currentStatus, task);
                case Manager -> handleManagerChangeStatus(taskDTO.getTaskStatus(), currentStatus, createdUserName, task);
                default -> false;
            };

            if (isValidChange) {
                updateTaskStatusAndHistory(taskDTO.getTaskStatus(), currentStatus, task, person);
            } else {
                throw new BadRequestException();
            }
        }

    private Task getValidatedTask(String title) {
        Optional<Task> optionalTask = taskDao.findByTitle(title);
        if (optionalTask.isEmpty()) {
            throw new BadRequestException();
        }
        return optionalTask.get();
    }

    private boolean handleEmployeeChangeStatus(Task.Status status, Task.Status currentStatus, Task task) {
        if (currentStatus == Task.Status.COMPLETED || task.getAssignee() == null) {
            return false;
        }

        if (status == Task.Status.IN_REVIEW && currentStatus == Task.Status.IN_PROGRESS) {
            Instant endTime = Instant.now();
            Instant startInstant = task.getStartTime().atZone(ZoneId.systemDefault()).toInstant();
            Instant endInstant = endTime.atZone(ZoneId.systemDefault()).toInstant();
            Duration duration = Duration.between(startInstant, endInstant);
            long minutes = duration.toMinutes();

            return minutes >= task.getTotal_time();
        } else if (status == Task.Status.IN_PROGRESS && currentStatus == Task.Status.CREATED) {
            task.setStartTime(Instant.now());
            return true;
        }
        return false;
    }

    private boolean handleManagerChangeStatus(Task.Status status, Task.Status currentStatus, String createdUserName, Task task) {
        return status == Task.Status.COMPLETED && currentStatus == Task.Status.IN_REVIEW && createdUserName.equals(task.getCreatedBy().getUsername());
    }

    private void updateTaskStatusAndHistory(Task.Status newStatus, Task.Status oldStatus, Task task, User person) {
        task.setTaskStatus(newStatus);
        TaskHistory history = new TaskHistory();
        Optional<User> optionalUser=userDao.getUserByUsername(person.getUsername());
        if(optionalUser.isPresent())
        {
            User user=optionalUser.get();
            history.setTimestamp(Instant.now());
            history.setOldStatus(oldStatus);
            history.setNewStatus(newStatus);
            history.setMovedBy(user);
            history.setTask(task);
            taskHistoryDao.save(history);
        }
        else {

            throw new ForbiddenAccessException();
        }

    }

    @Override
    public List<TaskDTO> getAllTasksCreatedByManager(Manager activeManager, Task.Status status) {

        List<Task> tasks = taskDao.getTasksByCreatedByUsernameAndTaskStatus(activeManager.getUsername(), status);
        List<TaskDTO> taskInfoList = new ArrayList<>();

        for (Task task : tasks) {

            TaskDTO taskInfo = new TaskDTO();
            taskInfo.setTitle(task.getTitle());
            taskInfo.setDescription(task.getDescription());
            taskInfo.setTaskStatus(task.getTaskStatus());
            Instant timestamp = task.getCreatedAt();

            taskInfo.setCreatedAt(timestamp);
            taskInfoList.add(taskInfo);
        }
        return taskInfoList;

    }

    @Override
    public List<TaskDTO> getAllTasksCreatedByManager(Manager activeManager, Task.Status status, String employeeName) {
        String[] nameParts = employeeName.split(" ");

        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        Optional<Employee> optionalEmployee = employeeDao.getEmployeeByFirstNameAndLastName(firstName,lastName);


        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();

            return getTaskDTOsByManagerAndStatus(activeManager, employee, status);
        } else {
            return Collections.emptyList();
        }
    }

    private List<TaskDTO> getTaskDTOsByManagerAndStatus(Manager activeManager, Employee employee, Task.Status status) {
        List<TaskDTO> taskByEmployeeAndStatuses = new ArrayList<>();
        List<Task> taskDTOs = taskDao.getTasksByCreatedByUsernameAndAssignee_UsernameAndTaskStatus(activeManager.getUsername(), employee.getUsername(), status);

        for (Task task : taskDTOs) {
            String assigneeName = getAssigneeName(task);
            Instant timestamp = task.getCreatedAt();

            TaskDTO taskByEmployeeAndStatus = new TaskDTO();
            taskByEmployeeAndStatus.setTitle(task.getTitle());
            taskByEmployeeAndStatus.setDescription(task.getDescription());
            taskByEmployeeAndStatus.setAssignee(assigneeName);
            taskByEmployeeAndStatus.setTaskStatus(task.getTaskStatus());
            taskByEmployeeAndStatus.setCreatedAt(timestamp);

            taskByEmployeeAndStatuses.add(taskByEmployeeAndStatus);
        }
        return taskByEmployeeAndStatuses;
    }

    private String getAssigneeName(Task task) {
        return task.getAssignee() != null ? task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName() : "N/A";
    }


    @Override
    public List<TaskDTO> getTasksByStatus(Employee employee) {
        List<TaskDTO> allTasks = new ArrayList<>();
        List<Task> tasks = taskDao.getTasksByAssignee_Username(employee.getUsername());

        for (Task task : tasks) {

            allTasks.add(createTaskDTO(task));
        }

        return allTasks;
    }

    @Override
    public List<TaskDTO> getAllTasksByStatus() {
        List<TaskDTO> allTasks = new ArrayList<>();
        List<Task> tasks = taskDao.findAll();

        for (Task task : tasks) {
            allTasks.add(createTaskDTO(task));
        }

        return allTasks;
    }

    private TaskDTO createTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskStatus(task.getTaskStatus());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setTitle(task.getTitle());
        Instant timestamp = task.getCreatedAt();
        taskDTO.setCreatedAt(timestamp);

        return taskDTO;
    }


    @Override
    public void assignTask(TaskDTO taskDTO, Manager manager) {
        Task task = taskDao.findByTitle(taskDTO.getTitle())
                .orElseThrow(BadRequestException::new);
        if (task.getAssignee()==null) {
            validateTaskAssignment(task, manager);

            Employee assignee = getAssigneeByName(taskDTO.getAssignee());
            task.setAssignee(assignee);
            task.setAssigned(true);
            taskDao.save(task);
        }
        else {

            throw new BadRequestException();
        }
    }



    private void validateTaskAssignment(Task task, Manager manager) {
        String assignerName = manager.getFirstName() + " " + manager.getLastName();
        String createdByFullName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();

        if (!assignerName.equals(createdByFullName)) {
            throw new ForbiddenAccessException();
        }

        if (task.isAssigned()) {
            throw new BadRequestException();
        }
    }

    private Employee getAssigneeByName(String fullName) {
        String[] nameParts = fullName.split(" ");

        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        Optional<Employee> optionalEmployee = employeeDao.getEmployeeByFirstNameAndLastName(firstName,lastName);
        return optionalEmployee.orElseThrow(NotFoundException::new);
    }

    @Override
    public List<TaskDTO> getTasksByUser(User.UserRole userRole) {
        List<TaskDTO> taskByEmployeeAndStatusDTOS = new ArrayList<>();

        List<Task> tasks =new ArrayList<>();
        if(userRole.equals(User.UserRole.Manager))
        {
             tasks=taskDao.findAll();
        }
        else if(userRole.equals(User.UserRole.Employee))
        {
            tasks = taskDao.getTasksByAssigneeNotNull();

        }
        else {

            tasks= Collections.emptyList();

        }

        for (Task task : tasks) {
            taskByEmployeeAndStatusDTOS.add(createTaskByEmployeeAndStatusDTO(task));
        }
        return taskByEmployeeAndStatusDTOS;
    }

    private TaskDTO createTaskByEmployeeAndStatusDTO(Task task) {
        TaskDTO taskByEmployeeAndStatusDTO = new TaskDTO();
        taskByEmployeeAndStatusDTO.setTitle(task.getTitle());
        taskByEmployeeAndStatusDTO.setDescription(task.getDescription());
        taskByEmployeeAndStatusDTO.setTaskStatus(task.getTaskStatus());
        if (task.getAssignee() != null) {
            taskByEmployeeAndStatusDTO.setAssignee(task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName());
        } else {
            taskByEmployeeAndStatusDTO.setAssignee("N/A");
        }
        Instant timestamp = task.getCreatedAt();
        taskByEmployeeAndStatusDTO.setCreatedAt(timestamp);

        taskByEmployeeAndStatusDTO.setCreatedBy(task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName());

        return taskByEmployeeAndStatusDTO;
    }


    @Override
    public List<TaskDTO> getAssignedTasks(Employee employee) {

        List<TaskDTO> assignedTasks = new ArrayList<>();
        List<Task> tasks = taskDao.getTasksByAssignee_Username(employee.getUsername());
        for (Task task : tasks) {
            TaskDTO assignTask = new TaskDTO();
            assignTask.setTaskStatus(task.getTaskStatus());
            Instant timestamp = task.getCreatedAt();
            assignTask.setCreatedAt(timestamp);
            assignTask.setDescription(task.getDescription());
            assignTask.setTitle(task.getTitle());
            assignTask.setTotal_time(task.getTotal_time());
            assignedTasks.add(assignTask);

        }

        return assignedTasks;

    }

    @Override
    public void archiveTask(TaskDTO taskDTO) {
        Task task = taskDao.findByTitle(taskDTO.getTitle())
                .orElseThrow(BadRequestException::new);

        String assigneeFullName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
        boolean isSameAssignee = taskDTO.getAssignee().equals(assigneeFullName);

        if (isSameAssignee) {
            throw new ForbiddenAccessException();
        }
        task.setAssigned(false);
        task.setAssignee(null);
        taskDao.save(task);
    }


//    private boolean taskDTOMatchesTask(TaskDTO taskDTO, Task task) {
//        return
//                Objects.equals(taskDTO.getTaskStatus(), task.getTaskStatus())
//                && Objects.equals(taskDTO.getCreatedBy(), task.getCreatedBy().getFirstName()+" "+task.getCreatedBy().getLastName())
//                && Objects.equals(taskDTO.getAssignee(), task.getAssignee().getFirstName()+" "+task.getAssignee().getLastName());
//
//    }
//
//    private boolean taskStatusDTOMatchesTask(TaskDTO taskDTO, Task task) {
//        return
//                Objects.equals(taskDTO.getCreatedBy(), task.getCreatedBy().getFirstName()+" "+task.getCreatedBy().getLastName())
//                && Objects.equals(taskDTO.getAssignee(), task.getAssignee().getFirstName()+" "+task.getAssignee().getLastName());
//
//    }
//    private boolean taskAssignDTOMatchesTask(TaskDTO taskDTO, Task task) {
//        return
//                 Objects.equals(taskDTO.getCreatedBy(), task.getCreatedBy().getFirstName()+" "+task.getCreatedBy().getLastName())
//                && Objects.equals(taskDTO.getTaskStatus(), task.getTaskStatus());
//
//    }


}
