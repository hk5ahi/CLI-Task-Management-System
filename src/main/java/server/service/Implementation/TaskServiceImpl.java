package server.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.dao.*;
import server.domain.*;
import server.dto.*;
import server.exception.*;

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
    @PersistenceContext
    private final EntityManager entityManager;
    private final UserDao userDao;
    private final TaskHistoryDao taskHistoryDao;
    private final UtilityService utilityService;
    private final EmployeeService employeeService;


    @Autowired
    public TaskServiceImpl(TaskDao taskDao, EmployeeDao employeeDao, EntityManager entityManager, UserDao userDao, TaskHistoryDao taskHistoryDao, UtilityService utilityService) {

        this.taskDao = taskDao;
        this.employeeDao = employeeDao;
        this.entityManager = entityManager;
        this.userDao = userDao;

        this.taskHistoryDao = taskHistoryDao;
        this.utilityService = utilityService;

    }

//
//    @Override
//    public List<TaskDTO> getAllTasksCreatedByManager(Manager manager, String employeeName) {
//        String[] nameParts = employeeName.split(" ");
//
//        String firstName = nameParts[0];
//        String lastName = nameParts.length > 1 ? nameParts[1] : "";
//        Optional<Employee> optionalEmployee = employeeDao.getEmployeeByFirstNameAndLastName(firstName, lastName);
//
//        if (optionalEmployee.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        Employee employee = optionalEmployee.get();
//        List<TaskDTO> taskByEmployees = new ArrayList<>();
//        List<Task> tasks = taskDao.getTasksByCreatedByUsernameAndAssignee_Username(manager.getUsername(), employee.getUsername());
//
//        for (Task task : tasks) {
//            TaskDTO taskByEmployee = createTaskDTOFromTask(task);
//            taskByEmployees.add(taskByEmployee);
//        }
//
//        return taskByEmployees;
//    }
//
//    private TaskDTO createTaskDTOFromTask(Task task) {
//        String creatorName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();
//        String assigneeName = "N/A"; // Default value in case assignee is null
//        if (task.getAssignee() != null) {
//            assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
//        }
//        Instant timestamp = task.getCreatedAt();
//
//        TaskDTO taskByEmployee = new TaskDTO();
//        taskByEmployee.setTitle(task.getTitle());
//        taskByEmployee.setDescription(task.getDescription());
//        taskByEmployee.setAssignee(assigneeName);
//        taskByEmployee.setCreatedAt(timestamp);
//        taskByEmployee.setCreatedBy(creatorName);
//
//        return taskByEmployee;
//    }
//

//    @Override
//    public List<TaskDTO> getAllTasksByUser(QueryParameterDTO queryParameterDTO) {
//        if (queryParameterDTO.getUserRole().equals(User.UserRole.Supervisor) && !queryParameterDTO.isAssigned() && queryParameterDTO.isNoCriteria() && !queryParameterDTO.isManagerRole() && !queryParameterDTO.isEmployeeRole() && !queryParameterDTO.isStatus()) {
//            String[] nameParts = queryParameterDTO.getEmployeeName().split(" ");
//
//
//            String firstName = nameParts[0];
//            String lastName = nameParts.length > 1 ? nameParts[1] : "";
//            Optional<Employee> optionalEmployee = employeeDao.getEmployeeByFirstNameAndLastName(firstName, lastName);
//
//            if (optionalEmployee.isEmpty()) {
//                return Collections.emptyList();
//            }
//
//            Employee employee = optionalEmployee.get();
//            List<TaskDTO> taskByEmployees = new ArrayList<>();
//            List<Task> tasks = taskDao.getTasksByAssignee_Username(employee.getUsername());
//
//            for (Task task : tasks) {
//                TaskDTO taskByEmployee = createTaskDTOFromTaskByUser(task);
//                taskByEmployees.add(taskByEmployee);
//            }
//
//            return taskByEmployees;
//        } else {
//
//            return Collections.emptyList();
//        }
//    }


//
//    private TaskDTO createTaskDTOFromTaskByUser(Task task) {
//        String assigneeName = task.getAssignee() != null ? task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName() : "N/A";
//        String creatorName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();
//        Instant timestamp = task.getCreatedAt();
//
//        TaskDTO taskByEmployee = new TaskDTO();
//        taskByEmployee.setTitle(task.getTitle());
//        taskByEmployee.setDescription(task.getDescription());
//        taskByEmployee.setAssignee(assigneeName);
//        taskByEmployee.setCreatedAt(timestamp);
//        taskByEmployee.setCreatedBy(creatorName);
//        taskByEmployee.setTaskStatus(task.getTaskStatus());
//        taskByEmployee.setTotal_time(task.getTotal_time());
//
//        return taskByEmployee;
//    }

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

    private void validateIfSupervisorCanViewAllTasks(String header,QueryParameterDTO queryParameterDTO)
    {
        UtilityService.AuthUserDTO authUserDTO = utilityService.getAuthUser(header);
        boolean isUserSupervisor = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Supervisor);
        boolean allTasksBySupervisor=queryParameterDTO.getUserRole().equals(User.UserRole.Supervisor);
        if(allTasksBySupervisor &&!isUserSupervisor)
        {
            log.error("The User {} is not a Supervisor", authUserDTO.getUsername());
            throw new ForbiddenAccessException("User is not able to validate the task");
        }

    }


    private void validateIfManagerCanViewAllTasks(String header,QueryParameterDTO queryParameterDTO)
    {
        UtilityService.AuthUserDTO authUserDTO = utilityService.getAuthUser(header);
        boolean isUserManager = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Manager);
        boolean allTasksByManager=queryParameterDTO.getUserRole().equals(User.UserRole.Manager);
        if(allTasksByManager &&!isUserManager)
        {
            log.error("The User {} is not a Manager", authUserDTO.getUsername());
            throw new ForbiddenAccessException("User is not able to validate the task");
        }

    }

    private void validateIfEmployeeCanViewAllTasks(String header,QueryParameterDTO queryParameterDTO)
    {
        UtilityService.AuthUserDTO authUserDTO = utilityService.getAuthUser(header);
        boolean isUserEmployee = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Employee);
        boolean allTasksByEmployee=queryParameterDTO.getUserRole().equals(User.UserRole.Employee);
        if(allTasksByEmployee &&!isUserEmployee)
        {
            log.error("The User {} is not a Employee", authUserDTO.getUsername());
            throw new ForbiddenAccessException("User is not able to validate the task");
        }

    }

    private List<Task> filterTasksByQueryParameters(QueryParameterDTO queryParams,String header) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM Task t WHERE 1 = 1");
        UtilityService.AuthUserDTO authUserDTO = utilityService.getAuthUser(header);
        Optional<User> optionalUser=utilityService.getUser(header);
        User user=null;
        if(optionalUser.isPresent())
        {
            user= optionalUser.get();
        }
//        Manager activeManager = utilityService.getActiveManager(header)
//                .orElseThrow(NotFoundException::new);
//        Employee activeEmployee = utilityService.getActiveEmployee(header)
//                .orElseThrow(NotFoundException::new);
        if (!(queryParams.getEmployeeName().equals("N/A")) && queryParams.getUserRole().equals(User.UserRole.Supervisor)) {
            jpql.append(" AND t.assignee = :assignee");
        }

        if (queryParams.getTaskStatus()!=null && queryParams.getUserRole().equals(User.UserRole.Supervisor)) {
            jpql.append(" AND t.taskStatus=:task_status");
        }


        if (queryParams.isEmployeeRole()) {
            jpql.append(" AND t.assignee is NOT NULL");
        }

        if (queryParams.isManagerRole() || queryParams.isStatus()) {
            jpql.append(" AND t.taskStatus IS NOT NULL");
        }

        if (queryParams.isEmployeeRole()) {
            jpql.append(" AND t.assignee is NOT NULL");
        }
        if(!(queryParams.getEmployeeName().equals("N/A"))  && queryParams.getUserRole().equals(User.UserRole.Manager))
        {
            jpql.append(" AND t.assignee = :assignee AND t.createdBy = :manager");

        }

        if(queryParams.getTaskStatus()!=null  && queryParams.getUserRole().equals(User.UserRole.Manager))
        {
            jpql.append(" AND t.taskStatus = :task_status AND t.createdBy = :manager");

        }

        if(queryParams.getTaskStatus()!=null  && queryParams.getUserRole().equals(User.UserRole.Manager) && !(queryParams.getEmployeeName().equals("N/A")))
        {
            jpql.append(" AND t.taskStatus = :task_status AND t.createdBy = :manager AND t.assignee = :assignee");

        }


        if (queryParams.isAssigned() &&  queryParams.getUserRole().equals(User.UserRole.Employee)) {
            jpql.append(" AND t.assignee =:assignee");
        }

        TypedQuery<Task> query = entityManager.createQuery(jpql.toString(), Task.class);

        if (!(queryParams.getEmployeeName().equals("N/A"))) {
            query.setParameter("assignee", getAssigneeByName(queryParams.getEmployeeName()));
        }
        if (queryParams.getTaskStatus()!=null && queryParams.getUserRole().equals(User.UserRole.Supervisor)) {
            query.setParameter("task_status", queryParams.getTaskStatus());
        }

        if (queryParams.getTaskStatus()!=null  && queryParams.getUserRole().equals(User.UserRole.Manager)) {
            query.setParameter("task_status", queryParams.getTaskStatus());
            query.setParameter("manager", user);
        }

        if (!(queryParams.getEmployeeName().equals("N/A"))  && queryParams.getUserRole().equals(User.UserRole.Manager)) {
            query.setParameter("assignee",  getAssigneeByName(queryParams.getEmployeeName()));
            query.setParameter("manager", user);
        }

        if (!(queryParams.getEmployeeName().equals("N/A"))  && queryParams.getUserRole().equals(User.UserRole.Manager) && queryParams.getTaskStatus()!=null) {
            query.setParameter("assignee",  getAssigneeByName(queryParams.getEmployeeName()));
            query.setParameter("manager", user);
            query.setParameter("task_status", queryParams.getTaskStatus());
        }

        if (queryParams.isAssigned() &&  queryParams.getUserRole().equals(User.UserRole.Employee)) {
            query.setParameter("assignee", user);
        }

        return query.getResultList();
    }

    @Override
    public List<TaskDTO> getTasksByController(QueryParameterDTO queryParameterDTO, String header) {


        validateIfSupervisorCanViewAllTasks(header,queryParameterDTO);
        validateIfManagerCanViewAllTasks(header,queryParameterDTO);
        validateIfEmployeeCanViewAllTasks(header,queryParameterDTO);

        List<Task> filterTasks=filterTasksByQueryParameters(queryParameterDTO,header);
        return mapTaskToTaskDTO(filterTasks);
//        if (utilityService.isAuthenticatedSupervisor(header) && User.UserRole.Supervisor.equals(queryParameterDTO.getUserRole())) {
//            return handleSupervisorTasks(queryParameterDTO);
//        } else if (utilityService.isAuthenticatedManager(header) && User.UserRole.Manager.equals(queryParameterDTO.getUserRole())) {
//            return handleManagerTasks(queryParameterDTO.isStatus(), queryParameterDTO.isAssigned(),queryParameterDTO.isEmployeeRole(),queryParameterDTO.isNoCriteria(),queryParameterDTO.isManagerRole(), queryParameterDTO.getEmployeeName(),queryParameterDTO.getTaskStatus(),header);
//        } else if (utilityService.isAuthenticatedEmployee(header) && User.UserRole.Employee.equals(queryParameterDTO.getUserRole())) {
//            return handleEmployeeTasks(queryParameterDTO.isAssigned(), queryParameterDTO.isStatus(), queryParameterDTO.isEmployeeRole(),queryParameterDTO.isNoCriteria(),header);
//        }
//
//        throw new ForbiddenAccessException();
    }

    List<TaskDTO> mapTaskToTaskDTO(List<Task> tasks)
    {
        List<TaskDTO> taskDTOS=new ArrayList<>();

        for(Task task:tasks)
        {
            TaskDTO taskDTO=new TaskDTO();
            taskDTO.setTitle(task.getTitle());
            taskDTO.setDescription(task.getDescription());
            taskDTO.setTaskStatus(task.getTaskStatus());
            if(task.getAssignee()!=null) {
                taskDTO.setAssignee(task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName());
            }
            else {
                taskDTO.setAssignee("N/A");
            }
            taskDTO.setCreatedAt(task.getCreatedAt());
            taskDTO.setCreatedBy(task.getCreatedBy().getFirstName()+" "+task.getCreatedBy().getLastName());
            taskDTO.setTotal_time(task.getTotal_time());
            taskDTOS.add(taskDTO);

        }

        return taskDTOS;

    }

//    private List<TaskDTO> validateGetAllTasksByUser(String header,QueryParameterDTO queryParameterDTO)
//    {
//        if (utilityService.isAuthenticatedSupervisor(header))
//        {
//            return getAllTasksByUser(queryParameterDTO);
//
//        }
//        else {
//
//            throw new BadRequestException();
//        }
//
//    }

//    private List<TaskDTO> validateGetAllTasksByStatus(String header,QueryParameterDTO queryParameterDTO)
//    {
//        if (utilityService.isAuthenticatedSupervisor(header))
//        {
//            return getAllTasksByStatus(queryParameterDTO);
//
//        }
//        else {
//
//            throw new BadRequestException();
//        }
//
//    }

//    private List<TaskDTO> validateGetTasksByUser(String header,QueryParameterDTO queryParameterDTO)
//    {
//        if (utilityService.isAuthenticatedSupervisor(header))
//        {
//            return validateUserRole(queryParameterDTO);
//
//        }
//        else {
//
//            throw new BadRequestException();
//        }
//
//    }

//    private List<TaskDTO> validateUserRole(QueryParameterDTO queryParameterDTO)
//    {
//        if(queryParameterDTO.isEmployeeRole() && !queryParameterDTO.isManagerRole())
//        {
//            return getTasksByUser(User.UserRole.Employee,queryParameterDTO);
//        } else if (!queryParameterDTO.isEmployeeRole() && queryParameterDTO.isManagerRole()) {
//            return getTasksByUser(User.UserRole.Manager,queryParameterDTO);
//        }
//        return null;
//    }

//    private List<TaskDTO> handleSupervisorTasks(QueryParameterDTO queryParameterDTO) {
//        if (queryParameterDTO.isNoCriteria()) {
//            return getTasksByUser(User.UserRole.Employee,queryParameterDTO);
//        } else if (queryParameterDTO.isStatus()) {
//            return getTasksByUser(User.UserRole.Manager,queryParameterDTO);
//        }
//        return Collections.emptyList();
//    }

//    private List<TaskDTO> handleManagerTasks(boolean status, boolean assigned, boolean employeeRole,
//                                                       boolean noCriteria, boolean manager, String employeeName,
//                                                       Task.Status taskStatus,String header) {
//        Manager activeManager = utilityService.getActiveManager(header)
//                .orElseThrow(BadRequestException::new);
//
//        if (status && !employeeRole && !noCriteria && !assigned && manager) {
//            return getAllTasksCreatedByManager(activeManager, taskStatus);
//        } else if (!status && employeeRole && !noCriteria && !assigned && manager && employeeName != null) {
//            return getAllTasksCreatedByManager(activeManager, employeeName);
//        } else if (status && employeeRole && !noCriteria && !assigned && manager) {
//            return getAllTasksCreatedByManager(activeManager, taskStatus, employeeName);
//        }
//        return Collections.emptyList();
//    }

//    private List<TaskDTO> handleEmployeeTasks(boolean assigned, boolean status, boolean employeeRole,
//                                                        boolean noCriteria,String header) {
//        Employee activeEmployee = utilityService.getActiveEmployee(header)
//                .orElseThrow(BadRequestException::new);
//
//        if (assigned && !status && !employeeRole && !noCriteria) {
//            return getAssignedTasks(activeEmployee);
//        } else if (!assigned && status && !employeeRole && !noCriteria) {
//            return getTasksByStatus(activeEmployee);
//        }
//        return Collections.emptyList();
//    }

    @Override
    public void updateTasksByController(String authorizationHeader, TaskDTO taskDTO) {

        validateTaskArchive(authorizationHeader,taskDTO);
        validateTaskStatus(authorizationHeader,taskDTO);
        validateTaskAssign(authorizationHeader,taskDTO);
        validateUpdateTime(authorizationHeader,taskDTO);
    }
    private void checkAndStartTime(Task prevTask,Task updatedtask)
    {
        boolean allowToStartTime=prevTask.getTaskStatus().equals(Task.Status.CREATED) && updatedtask.getTaskStatus().equals(Task.Status.IN_PROGRESS);
        if(allowToStartTime)
        {
            updatedtask.setStartTime(Instant.now());
            taskDao.save(updatedtask);
        }

    }

    private String getAssigneeFullName(Task existedTask) {
    if (existedTask.getAssignee() != null) {
        return existedTask.getAssignee().getFirstName() + " " + existedTask.getAssignee().getLastName();
    } else {
        return "N/A";
    }
}
    private void validateIfUserCanAssignTask(String authorizationHeader, TaskDTO taskDTO,Task existedTask)
    {
        UtilityService.AuthUserDTO authUserDTO = utilityService.getAuthUser(authorizationHeader);
        if(taskDTO.getAssignee()!=null) {
            String[] nameParts = taskDTO.getAssignee().split(" ");

            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";
            boolean isTaskNeedToAssign = !Objects.equals(taskDTO.getAssignee(), getAssigneeFullName(existedTask));
            boolean isUserManager = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Manager);
            String assignerUserName = authUserDTO.getUsername();
            String createdByUserName = existedTask.getCreatedBy().getUsername();
            boolean assigneeExist = userDao.existsByFirstNameAndLastName(firstName, lastName);
            boolean accessToAssign = assignerUserName.equals(createdByUserName);

            if (isTaskNeedToAssign && taskDTO.getArchived()) {
                log.error("The assigning of Task and archiving cant be done at same time.");
                throw new ForbiddenAccessException("User is not able to validate the task");

            }
            else if (isTaskNeedToAssign && !isUserManager) {
                log.error("Only Manager can assign a task. User {} is not a Manager", authUserDTO);
                throw new ForbiddenAccessException("User is not able to validate the task");

            }
            else if (isTaskNeedToAssign && !accessToAssign) {

                log.error("The Manager {} has not access to assign the Task", authUserDTO.getUsername());
                throw new ForbiddenAccessException("User is not able to validate the task");

            } else if (isTaskNeedToAssign && !assigneeExist) {
                log.error("The Employee does not exist");
                throw new ForbiddenAccessException("User is not able to validate the task");

            }  else if (isTaskNeedToAssign && taskDTO.getAssignee().equals(createdByUserName)) {
                log.error("The Task can not be assigned to the manager {} which has created that task", authUserDTO.getUsername());
                throw new ForbiddenAccessException("User is not able to validate the task");
            }
        }

    }
    private void validateTaskArchive(String authorizationHeader, TaskDTO taskDTO) {
        if (utilityService.isAuthenticatedSupervisor(authorizationHeader)) {

            archiveTask(taskDTO);
        } else {
            throw new ForbiddenAccessException();
        }
    }

    private void validateIfUserCanChangeStatus(String authorizationHeader, TaskDTO taskDTO,Task existedTask) {
        UtilityService.AuthUserDTO authUserDTO = utilityService.getAuthUser(authorizationHeader);

        Task.Status currentStatus = existedTask.getTaskStatus();
        Task.Status toBeUpdatedStatus = taskDTO.getTaskStatus();
        boolean isTaskNeedToChangeStatus = !Objects.equals(currentStatus,toBeUpdatedStatus);
        boolean isUserSupervisor = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Supervisor);
        boolean isUserManager = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Manager);
        boolean isUserEmployee = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Employee);
        boolean changeTaskAccess=existedTask.getAssignee()!=null && existedTask.getAssignee().getUsername().equals(authUserDTO.getUsername());
        if (isTaskNeedToChangeStatus && currentStatus == Task.Status.CREATED &&( taskDTO.getTaskStatus()== Task.Status.IN_REVIEW || taskDTO.getTaskStatus()== Task.Status.COMPLETED )) {
            log.error(" Task is in undesirable State");
            throw new BadRequestException("User is not able to validate the task");
        }
        else if (isTaskNeedToChangeStatus && (currentStatus == Task.Status.COMPLETED || existedTask.getAssignee() == null)) {
            log.error("Task is not assigned yet or Task is  in undesirable State");
            throw new BadRequestException("User is not able to validate the task");
        }
        else if(isTaskNeedToChangeStatus && !changeTaskAccess && !isUserManager)
        {
            log.error("You do not have access to change Status");
            throw new ForbiddenAccessException("User is not able to validate the task");

        }

        else if(isTaskNeedToChangeStatus && currentStatus.equals(Task.Status.IN_PROGRESS) && toBeUpdatedStatus.equals(Task.Status.IN_REVIEW))
        {
            Instant endTime = Instant.now();
            Instant startInstant = existedTask.getStartTime().atZone(ZoneId.systemDefault()).toInstant();
            Instant endInstant = endTime.atZone(ZoneId.systemDefault()).toInstant();
            Duration duration = Duration.between(startInstant, endInstant);
            long minutes = duration.toMinutes();

            if(!(minutes >= existedTask.getTotal_time()))
            {
                log.error("The time to change status from IN_PROGRESS to IN_REVIEW is not yet reached.The remaining time is {}",existedTask.getTotal_time()-minutes);
                throw new BadRequestException("User is not able to validate the task");
            }

        }

        else if (isTaskNeedToChangeStatus && isUserSupervisor) {
            log.error("Only Manager and Employee can update Task Status. User {} is not a Manager/Employee", authUserDTO);
            throw new ForbiddenAccessException("User is not able to validate the task");

        } else if (isTaskNeedToChangeStatus && isUserManager && (toBeUpdatedStatus.equals(Task.Status.IN_PROGRESS) || toBeUpdatedStatus.equals(Task.Status.IN_REVIEW))) {
            log.error("Only Employee can update Task Status from CREATED to IN_PROGRESS/IN_REVIEW. User {} is not a Employee", authUserDTO);
            throw new ForbiddenAccessException("User is not able to validate the task");

        } else if (isTaskNeedToChangeStatus && isUserEmployee && toBeUpdatedStatus.equals(Task.Status.COMPLETED)) {
            log.error("Only Manager can update Task Status from IN_REVIEW to COMPLETED. User {} is not a Manager", authUserDTO);
            throw new ForbiddenAccessException("User is not able to validate the task");

        }

    }

    private void checkAndCreateTaskHistory(Task prevTask,Task updatedTask,String header)
    {
        boolean changedStatus=!(prevTask.getTaskStatus().equals(updatedTask.getTaskStatus()));
        UtilityService.AuthUserDTO authUserDTO = utilityService.getAuthUser(header);
        if(changedStatus) {
            TaskHistory history = new TaskHistory();
            Optional<User> optionalUser = userDao.getUserByUsername(authUserDTO.getUsername());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                history.setTimestamp(Instant.now());
                history.setOldStatus(prevTask.getTaskStatus());
                history.setNewStatus(updatedTask.getTaskStatus());
                history.setMovedBy(user);
                history.setTask(updatedTask);
                taskHistoryDao.save(history);
            }
        }

    }

    private void validateUpdateTime(String header,TaskDTO taskDTO,Task existedTask)

    {
    UtilityService.AuthUserDTO authUserDTO = utilityService.getAuthUser(header);

    boolean needToChangeTime=!(existedTask.getTotal_time()==taskDTO.getTotal_time());
    boolean isUserEmployee = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Employee);
    String assigneeUserName;
    if(existedTask.getAssignee()!=null)
    {
        assigneeUserName= existedTask.getAssignee().getUsername();
    }
    else {
        assigneeUserName="N/A";
    }
    boolean accessToUpdate=assigneeUserName.equals(authUserDTO.getUsername());

    if(needToChangeTime && !isUserEmployee )
    {
        log.error("The Employee only can update total time");
        throw new ForbiddenAccessException("User is not able to validate the task");
    }
    else if (needToChangeTime && !accessToUpdate)
    {
        log.error("The employee {} dont have access to update it.",authUserDTO.getUsername());
        throw new ForbiddenAccessException("User is not able to validate the task");

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


//    @Override
//    public List<TaskDTO> getAllTasksCreatedByManager(Manager activeManager, Task.Status status) {
//
//        List<Task> tasks = taskDao.getTasksByCreatedByUsernameAndTaskStatus(activeManager.getUsername(), status);
//        List<TaskDTO> taskInfoList = new ArrayList<>();
//
//        for (Task task : tasks) {
//
//            TaskDTO taskInfo = new TaskDTO();
//            taskInfo.setTitle(task.getTitle());
//            taskInfo.setDescription(task.getDescription());
//            taskInfo.setTaskStatus(task.getTaskStatus());
//            Instant timestamp = task.getCreatedAt();
//
//            taskInfo.setCreatedAt(timestamp);
//            taskInfoList.add(taskInfo);
//        }
//        return taskInfoList;
//
//    }

//    @Override
//    public List<TaskDTO> getAllTasksCreatedByManager(Manager activeManager, Task.Status status, String employeeName) {
//        String[] nameParts = employeeName.split(" ");
//
//        String firstName = nameParts[0];
//        String lastName = nameParts.length > 1 ? nameParts[1] : "";
//        Optional<Employee> optionalEmployee = employeeDao.getEmployeeByFirstNameAndLastName(firstName,lastName);
//
//
//        if (optionalEmployee.isPresent()) {
//            Employee employee = optionalEmployee.get();
//
//            return getTaskDTOsByManagerAndStatus(activeManager, employee, status);
//        } else {
//            return Collections.emptyList();
//        }
//    }
//
//    private List<TaskDTO> getTaskDTOsByManagerAndStatus(Manager activeManager, Employee employee, Task.Status status) {
//        List<TaskDTO> taskByEmployeeAndStatuses = new ArrayList<>();
//        List<Task> taskDTOs = taskDao.getTasksByCreatedByUsernameAndAssignee_UsernameAndTaskStatus(activeManager.getUsername(), employee.getUsername(), status);
//
//        for (Task task : taskDTOs) {
//            String assigneeName = getAssigneeName(task);
//            Instant timestamp = task.getCreatedAt();
//
//            TaskDTO taskByEmployeeAndStatus = new TaskDTO();
//            taskByEmployeeAndStatus.setTitle(task.getTitle());
//            taskByEmployeeAndStatus.setDescription(task.getDescription());
//            taskByEmployeeAndStatus.setAssignee(assigneeName);
//            taskByEmployeeAndStatus.setTaskStatus(task.getTaskStatus());
//            taskByEmployeeAndStatus.setCreatedAt(timestamp);
//
//            taskByEmployeeAndStatuses.add(taskByEmployeeAndStatus);
//        }
//        return taskByEmployeeAndStatuses;
//    }

//    private String getAssigneeName(Task task) {
//        return task.getAssignee() != null ? task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName() : "N/A";
//    }


//    @Override
//    public List<TaskDTO> getTasksByStatus(Employee employee) {
//        List<TaskDTO> allTasks = new ArrayList<>();
//        List<Task> tasks = taskDao.getTasksByAssignee_Username(employee.getUsername());
//
//        for (Task task : tasks) {
//
//            allTasks.add(createTaskDTO(task));
//        }
//
//        return allTasks;
//    }

//    @Override
//    public List<TaskDTO> getAllTasksByStatus(QueryParameterDTO queryParameterDTO) {
//        if (!queryParameterDTO.isAssigned()&& queryParameterDTO.isStatus() && !queryParameterDTO.isManagerRole() && !queryParameterDTO.isEmployeeRole() && !queryParameterDTO.isNoCriteria()) {
//
//            List<TaskDTO> allTasks = new ArrayList<>();
//            List<Task> tasks = taskDao.findAll();
//
//            for (Task task : tasks) {
//                allTasks.add(createTaskDTO(task));
//            }
//
//            return allTasks;
//        }
//        else {
//
//            return Collections.emptyList();
//        }
//    }

    private TaskDTO createTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskStatus(task.getTaskStatus());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setTitle(task.getTitle());
        Instant timestamp = task.getCreatedAt();
        taskDTO.setCreatedAt(timestamp);

        return taskDTO;
    }



    private Employee getAssigneeByName(String fullName) {
        if(fullName!=null) {
            String[] nameParts = fullName.split(" ");

            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";
            Optional<Employee> optionalEmployee = employeeDao.getEmployeeByFirstNameAndLastName(firstName, lastName);
            return optionalEmployee.orElseThrow(NotFoundException::new);
        }
        else {
            return null;
        }
    }

//    @Override
//    public List<TaskDTO> getTasksByUser(User.UserRole userRole,QueryParameterDTO queryParameterDTO) {
//        if (!queryParameterDTO.isAssigned() && queryParameterDTO.isStatus()  && !queryParameterDTO.isNoCriteria()) {
//            List<TaskDTO> taskByEmployeeAndStatusDTOS = new ArrayList<>();
//
//            List<Task> tasks = new ArrayList<>();
//            if (userRole.equals(User.UserRole.Manager)) {
//                tasks = taskDao.findAll();
//            } else if (userRole.equals(User.UserRole.Employee)) {
//                tasks = taskDao.getTasksByAssigneeNotNull();
//
//            } else {
//
//                tasks = Collections.emptyList();
//
//            }
//
//            for (Task task : tasks) {
//                taskByEmployeeAndStatusDTOS.add(createTaskByEmployeeAndStatusDTO(task));
//            }
//            return taskByEmployeeAndStatusDTOS;
//        }
//        else {
//
//            return Collections.emptyList();
//        }
//    }

//    private TaskDTO createTaskByEmployeeAndStatusDTO(Task task) {
//        TaskDTO taskByEmployeeAndStatusDTO = new TaskDTO();
//        taskByEmployeeAndStatusDTO.setTitle(task.getTitle());
//        taskByEmployeeAndStatusDTO.setDescription(task.getDescription());
//        taskByEmployeeAndStatusDTO.setTaskStatus(task.getTaskStatus());
//        if (task.getAssignee() != null) {
//            taskByEmployeeAndStatusDTO.setAssignee(task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName());
//        } else {
//            taskByEmployeeAndStatusDTO.setAssignee("N/A");
//        }
//        Instant timestamp = task.getCreatedAt();
//        taskByEmployeeAndStatusDTO.setCreatedAt(timestamp);
//
//        taskByEmployeeAndStatusDTO.setCreatedBy(task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName());
//
//        return taskByEmployeeAndStatusDTO;
//    }


//    @Override
//    public List<TaskDTO> getAssignedTasks(Employee employee) {
//
//        List<TaskDTO> assignedTasks = new ArrayList<>();
//        List<Task> tasks = taskDao.getTasksByAssignee_Username(employee.getUsername());
//        for (Task task : tasks) {
//            TaskDTO assignTask = new TaskDTO();
//            assignTask.setTaskStatus(task.getTaskStatus());
//            Instant timestamp = task.getCreatedAt();
//            assignTask.setCreatedAt(timestamp);
//            assignTask.setDescription(task.getDescription());
//            assignTask.setTitle(task.getTitle());
//            assignTask.setTotal_time(task.getTotal_time());
//            assignedTasks.add(assignTask);
//
//        }
//
//        return assignedTasks;
//
//    }


}
