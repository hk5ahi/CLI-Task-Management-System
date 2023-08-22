package server.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserDao userDao;
    private final TaskHistoryDao taskHistoryDao;
    private final UtilityService utilityService;
    private final EmployeeService employeeService;


    @Autowired
    public TaskServiceImpl(TaskDao taskDao, UserDao userDao, TaskHistoryDao taskHistoryDao, UtilityService utilityService) {

        this.taskDao = taskDao;
        this.userDao = userDao;
        this.taskHistoryDao = taskHistoryDao;
        this.utilityService = utilityService;

    }
    private void storeTask(Manager activeManager, TaskDTO taskDTO) {

        Task task = new Task(taskDTO.getTitle(), taskDTO.getDescription(), taskDTO.getTotal_time());
        task.setCreatedBy(activeManager);
        Instant currentInstant = Instant.now();
        task.setCreatedAt(currentInstant);
        if (taskDao.existsByTitle(task.getTitle())) {

            log.error("The task already exists with same title {}",task.getTitle());
            throw new ForbiddenAccessException("The task can not be created");
        } else {
            taskDao.save(task);

        }
    }

    private void validateIfSupervisorCanViewAllTasks(String header,QueryParameterDTO queryParameterDTO)
    {
        AuthUserDTO authUserDTO = utilityService.getAuthUser(header);
        boolean isUserSupervisor = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Supervisor);
        boolean allTasksBySupervisor=queryParameterDTO.getByUserRole().equals(User.UserRole.Supervisor);
        if(allTasksBySupervisor &&!isUserSupervisor)
        {
            log.error("The User {} is not a Supervisor", authUserDTO.getUsername());
            throw new ForbiddenAccessException("User is not able to validate the task");
        }

    }


    private void validateIfManagerCanViewAllTasks(String header,QueryParameterDTO queryParameterDTO)
    {
        AuthUserDTO authUserDTO = utilityService.getAuthUser(header);
        boolean isUserManager = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Manager);
        boolean allTasksByManager=queryParameterDTO.getByUserRole().equals(User.UserRole.Manager);
        if(allTasksByManager &&!isUserManager)
        {
            log.error("The User {} is not a Manager", authUserDTO.getUsername());
            throw new ForbiddenAccessException("User is not able to validate the task");
        }

    }

    private void validateIfEmployeeCanViewAllTasks(String header,QueryParameterDTO queryParameterDTO)
    {
        AuthUserDTO authUserDTO = utilityService.getAuthUser(header);
        boolean isUserEmployee = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Employee);
        boolean allTasksByEmployee=queryParameterDTO.getByUserRole().equals(User.UserRole.Employee);
        if(allTasksByEmployee &&!isUserEmployee)
        {
            log.error("The User {} is not a Employee", authUserDTO.getUsername());
            throw new ForbiddenAccessException("User is not able to validate the task");
        }

    }

    @Override
    public List<TaskDTO> getTasks(QueryParameterDTO queryParameterDTO, String header) {

        validateIfSupervisorCanViewAllTasks(header,queryParameterDTO);
        validateIfManagerCanViewAllTasks(header,queryParameterDTO);
        validateIfEmployeeCanViewAllTasks(header,queryParameterDTO);
        List<Task> filterTasks=taskDao.filterTasksByQueryParameters(queryParameterDTO,header);
        return mapTaskToTaskDTO(filterTasks);

    }

    private List<TaskDTO> mapTaskToTaskDTO(List<Task> tasks)
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


    @Override
    @Transactional
    public void updateTask(String authorizationHeader, TaskDTO taskDTO) {

        validateTaskArchive(authorizationHeader,taskDTO);
        validateTaskStatus(authorizationHeader,taskDTO);
        validateTaskAssign(authorizationHeader,taskDTO);
        validateUpdateTime(authorizationHeader,taskDTO);
    }
    private void checkAndStartTime(Task prevTask, Task updatedtask)
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
        AuthUserDTO authUserDTO = utilityService.getAuthUser(authorizationHeader);
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
                throw new BadRequestException("User is not able to validate the task");

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
                throw new BadRequestException("User is not able to validate the task");

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
        AuthUserDTO authUserDTO = utilityService.getAuthUser(authorizationHeader);
        Task.Status currentStatus = existedTask.getTaskStatus();
        Task.Status toBeUpdatedStatus = taskDTO.getTaskStatus();
        boolean isTaskNeedToChangeStatus = !Objects.equals(currentStatus, toBeUpdatedStatus);
        boolean isUserSupervisor = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Supervisor);
        boolean isUserManager = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Manager);
        boolean isUserEmployee = Objects.equals(authUserDTO.getUserRole(), User.UserRole.Employee);
        boolean changeTaskAccess = existedTask.getAssignee() != null && existedTask.getAssignee().getUsername().equals(authUserDTO.getUsername());
        if (isTaskNeedToChangeStatus && currentStatus == Task.Status.CREATED && (taskDTO.getTaskStatus() == Task.Status.IN_REVIEW || taskDTO.getTaskStatus() == Task.Status.COMPLETED)) {
            log.error(" Task is in undesirable State");
            throw new BadRequestException("User is not able to validate the task");
        } else if (isTaskNeedToChangeStatus && (currentStatus == Task.Status.COMPLETED || existedTask.getAssignee() == null)) {
            log.error("Task is not assigned yet or Task is  in undesirable State");
            throw new BadRequestException("User is not able to validate the task");
        } else if (isTaskNeedToChangeStatus && !changeTaskAccess && !isUserManager) {
            log.error("You do not have access to change Status");
            throw new ForbiddenAccessException("User is not able to validate the task");
        } else if (isTaskNeedToChangeStatus && currentStatus.equals(Task.Status.IN_PROGRESS) && toBeUpdatedStatus.equals(Task.Status.IN_REVIEW)) {
            Instant endTime = Instant.now();
            Instant startInstant = existedTask.getStartTime().atZone(ZoneId.systemDefault()).toInstant();
            Instant endInstant = endTime.atZone(ZoneId.systemDefault()).toInstant();
            Duration duration = Duration.between(startInstant, endInstant);
            long minutes = duration.toMinutes();

            if (!(minutes >= existedTask.getTotal_time())) {
                log.error("The time to change status from IN_PROGRESS to IN_REVIEW is not yet reached.The remaining time is {}", existedTask.getTotal_time() - minutes);
                throw new BadRequestException("User is not able to validate the task");
            }
        } else if (isTaskNeedToChangeStatus && isUserSupervisor) {
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

    private void checkAndCreateTaskHistory(Task prevTask, Task updatedTask, String header)
    {
        boolean changedStatus=!(prevTask.getTaskStatus().equals(updatedTask.getTaskStatus()));
        AuthUserDTO authUserDTO = utilityService.getAuthUser(header);
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
    AuthUserDTO authUserDTO = utilityService.getAuthUser(header);

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
    @Transactional
    public void createTask(TaskDTO task, String header) {
        if (utilityService.isAuthenticatedManager(header)) {
            Manager activeManager = utilityService.getActiveManager(header)
                    .orElseThrow(ForbiddenAccessException::new);

            storeTask(activeManager, task);
        } else {
            log.error("The requesting user is not a Manager.");
            throw new ForbiddenAccessException("Only Manager can create a Task");
        }
    }

}