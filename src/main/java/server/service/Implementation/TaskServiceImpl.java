package server.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.dao.TaskDao;

import server.dao.TaskHistoryDao;
import server.domain.*;
import server.dto.*;
import server.exception.ForbiddenAccessException;
import server.service.EmployeeService;
import server.service.ManagerService;
import server.service.TaskService;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Service
public class TaskServiceImpl implements TaskService {

    private final EmployeeService employeeService;
    private final ManagerService managerService;
    private final TaskDao taskDao;
    private final EmployeeDao employeeDao;

    private final ManagerDao managerDao;
    private final TaskHistoryDao taskHistoryDao;


    @Autowired
    public TaskServiceImpl(EmployeeService employeeService, ManagerService managerService, TaskDao taskDao, EmployeeDao employeeDao, ManagerDao managerDao, TaskHistoryDao taskHistoryDao) {
        this.employeeService = employeeService;
        this.managerService = managerService;
        this.taskDao = taskDao;
        this.employeeDao = employeeDao;
        this.managerDao = managerDao;
        this.taskHistoryDao = taskHistoryDao;
    }


    @Override
    public List<Task> getAllTasks() {

        List<Task> allTasks = taskDao.getAll();

        return allTasks;
    }

    @Override
    public List<TaskDTO> viewAllTasksCreatedByManager(Manager manager, String employeeName) {
        Employee employee = (Employee) employeeDao.getEmployeeByName(employeeName);
        if (employee != null) {

            List<TaskDTO> taskbyEmployees = new ArrayList<>();
            List<Task> allTasks = taskDao.getAll();


            for (Task task : allTasks) {
                String assigneeName = "N/A"; // Default value in case assignee is null
                if (task.getAssignee() != null) {
                    assigneeName = task.getAssignee().getUsername();
                }

                String creatorUserName = task.getCreatedBy().getUsername();
                String creatorName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();

                if (manager.getUsername().equals(creatorUserName) && employee.getUsername().equals(assigneeName)) {

                    Instant timestamp = task.getCreatedAt();
                    LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = localDateTime.format(formatter);
                    TaskDTO taskbyEmployee = new TaskDTO();
                    taskbyEmployee.setTitle(task.getTitle());
                    taskbyEmployee.setDescription(task.getDescription());
                    taskbyEmployee.setAssignee(assigneeName);
                    taskbyEmployee.setCreatedAt(formattedDateTime);
                    taskbyEmployee.setCreatedBy(creatorName);

                    taskbyEmployees.add(taskbyEmployee);
                }
            }
            return taskbyEmployees;
        } else {

            return null;
        }
    }



    @Override
    public List<TaskDTO> viewAllTasksByUser(String employeeName) {

        Employee employee= (Employee) employeeDao.getEmployeeByName(employeeName);
        if(employee!=null) {
            List<TaskDTO> taskbyEmployees = new ArrayList<>();
            List<Task> allTasks = taskDao.getAll();

            for (Task task : allTasks) {
                String assigneeName = "N/A"; // Default value in case assignee is null
                if (task.getAssignee() != null) {
                    assigneeName = task.getAssignee().getUsername();
                }

                String creatorName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();

                if (employee.getUsername().equals(assigneeName)) {
                    Instant timestamp = task.getCreatedAt();
                    LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = localDateTime.format(formatter);
                    TaskDTO taskbyEmployee = new TaskDTO();


                    taskbyEmployee.setTitle(task.getTitle());
                    taskbyEmployee.setDescription(task.getDescription());
                    taskbyEmployee.setAssignee(assigneeName);
                    taskbyEmployee.setCreatedAt(formattedDateTime);
                    taskbyEmployee.setCreatedBy(creatorName);
                    taskbyEmployee.setTaskStatus(task.getTaskStatus());
                    taskbyEmployee.setTotal_time(task.getTotal_time());

                    taskbyEmployees.add(taskbyEmployee);
                }
            }
            return taskbyEmployees;
        }
        else {

            return null;
        }
    }


    @Override
    public ResponseEntity<String> createTask(Manager activeManager, String title, String description, double total_time) {
        Task task = new Task(title, description, total_time);
        task.setCreatedBy(activeManager);
        Instant currentInstant = Instant.now();
        task.setCreatedAt(currentInstant); // Store the formatted timestamp
        for(Task taskinDoa:taskDao.getAll())
        {
            if(taskinDoa.getTitle().equals(task.getTitle()))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        }
        taskDao.addTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);

    }

    @Override
    public ResponseEntity<String> changeTaskStatus(String title, Task.Status status, User person) {
        Task task = getTaskByTitle(title);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Task.Status currentStatus = task.getTaskStatus();

        if (currentStatus == Task.Status.COMPLETED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (task.getAssignee() == null) {
            throw new ForbiddenAccessException();
        }

        String assigneeFullName = task.getAssignee().getFirstName()+" "+task.getAssignee().getLastName();
        String personFullName = person.getFirstName()+" "+person.getLastName();
        String createdFullName = task.getCreatedBy().getFirstName()+" "+task.getCreatedBy().getLastName();

        boolean isValidChange = false;

        switch (person.getUserRole()) {
            case "Employee":
                if (!assigneeFullName.equals(personFullName)) {
                    throw new ForbiddenAccessException();
                }

                if (status == Task.Status.IN_REVIEW && currentStatus == Task.Status.IN_PROGRESS) {
                    Instant endTime = Instant.now();
                    Instant startInstant = task.getStartTime().atZone(ZoneId.systemDefault()).toInstant();
                    Instant endInstant = endTime.atZone(ZoneId.systemDefault()).toInstant();
                    Duration duration = Duration.between(startInstant, endInstant);
                    long minutes = duration.toMinutes();

                    if (minutes >= task.getTotal_time()) {
                        isValidChange = true;
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                } else if (status == Task.Status.IN_PROGRESS && currentStatus == Task.Status.CREATED) {
                    task.setStartTime(Instant.now());
                    isValidChange = true;
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
                break;

            case "Manager":
                if (!createdFullName.equals(personFullName)) {
                    throw new ForbiddenAccessException();
                }

                if (status == Task.Status.COMPLETED && currentStatus == Task.Status.IN_REVIEW) {
                    isValidChange = true;
                } else {
                    throw new ForbiddenAccessException();
                }
                break;

            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (isValidChange) {
            task.setTaskStatus(status);
            TaskHistory history = new TaskHistory();
            User user = new User();
            user.setFirstName(person.getFirstName());
            user.setLastName(person.getLastName());
            history.setTimestamp(Instant.now());
            history.setOldStatus(currentStatus);
            history.setNewStatus(status);
            history.setMovedBy(user);
            taskHistoryDao.setTaskHistory(history,task);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public List<TaskDTO> viewAllTasksCreatedByManager(Manager activeManager,Task.Status status) {

        List<Task> allTasks = taskDao.getAll();
        List<TaskDTO> taskInfoList = new ArrayList<>();

        for (Task task : allTasks) {
            String createdBy = task.getCreatedBy().getUsername();

            if (createdBy.equals(activeManager.getUsername()) && task.getTaskStatus().equals(status)) {
                TaskDTO taskInfo = new TaskDTO();
                taskInfo.setTitle(task.getTitle());
                taskInfo.setDescription(task.getDescription());
                taskInfo.setTaskStatus(task.getTaskStatus());
                Instant timestamp = task.getCreatedAt();
                LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                String formattedDateTime = localDateTime.format(formatter);
                taskInfo.setCreatedAt(formattedDateTime);
                taskInfoList.add(taskInfo);
            }
        }
        if (taskInfoList.isEmpty()) {

            return null;
        }

        return taskInfoList;
    }



    @Override
    public List<TaskDTO> viewAllTasksCreatedByManager(Manager activeManager,Task.Status status,String employeeName) {

        Employee employee= (Employee) employeeDao.getEmployeeByName(employeeName);
        if(employee!=null) {
            List<TaskDTO> taskbyEmployeeandStatuses = new ArrayList<>();

            for (Task task : taskDao.getAll()) {
                String createdBy = "N/A"; // Default value in case createdBy is null
                String assigneeName = "N/A"; // Default value in case assignee is null

                if (task.getCreatedBy() != null) {
                    createdBy = task.getCreatedBy().getUsername();
                }

                if (task.getAssignee() != null) {
                    assigneeName = task.getAssignee().getUsername();
                }

                if (createdBy.equals(activeManager.getUsername()) && (task.getTaskStatus().equals(status)) && assigneeName.equals(employee.getUsername())) {

                    Instant timestamp = task.getCreatedAt();
                    LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


                    String formattedDateTime = localDateTime.format(formatter);
                    TaskDTO taskbyEmployeeandStatus = new TaskDTO();
                    taskbyEmployeeandStatus.setTitle(task.getTitle());
                    taskbyEmployeeandStatus.setDescription(task.getDescription());
                    taskbyEmployeeandStatus.setAssignee(assigneeName);
                    taskbyEmployeeandStatus.setTaskStatus(task.getTaskStatus());
                    taskbyEmployeeandStatus.setCreatedAt(formattedDateTime);

                    taskbyEmployeeandStatuses.add(taskbyEmployeeandStatus);

                }
            }


            return (taskbyEmployeeandStatuses);
        }
        else {

            return null;
        }
    }

    @Override
    public List<TaskDTO> viewTasksByStatus(Employee employee) {
        List<TaskDTO> allTasks = new ArrayList<>();
        List<Task> tasks = taskDao.getAll();

        for (Task task : tasks) {
            if (isTaskAssignedToEmployee(task, employee)) {
                allTasks.add(createTaskDTO(task));
            }
        }

        return allTasks;
    }

    @Override
    public List<TaskDTO> viewAllTasksByStatus() {
        List<TaskDTO> allTasks = new ArrayList<>();
        List<Task> tasks = taskDao.getAll();

        for (Task task : tasks) {
            allTasks.add(createTaskDTO(task));
        }

        return allTasks;
    }

    private boolean isTaskAssignedToEmployee(Task task, Employee employee) {
        String assigneeUserName;
        if(task.getAssignee()!=null) {
             assigneeUserName = task.getAssignee().getUsername();
        }
        else {
             assigneeUserName="N/A";
        }
        String employeeUserName = employee.getUsername();
        return assigneeUserName.equals(employeeUserName);
    }

    private TaskDTO createTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskStatus(task.getTaskStatus());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setTitle(task.getTitle());

        Instant timestamp = task.getCreatedAt();
        LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        taskDTO.setCreatedAt(formattedDateTime);

        return taskDTO;
    }



    @Override
    public ResponseEntity<String> assignTask(String title, String fullName,Manager manager) {
        Task task = getTaskByTitle(title);
        if(task!=null) {
            String assignerName = manager.getFirstName() + " " + manager.getLastName();
            if (!(assignerName.equals(task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName()))) {
                throw new ForbiddenAccessException();

            }
            if (task.equals(null)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Manager managerByName = (Manager) managerDao.getManagerByName(fullName);

            if (managerByName != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            User assignee = employeeDao.getEmployeeByName(fullName);
            if (assignee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (task.isAssigned()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                task.setAssignee(assignee);
                task.setAssigned(true);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
        }
        else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @Override
    public List<TaskDTO> viewTasksByUser(String userRole) {
        List<TaskDTO> taskbyEmployeeandStatusDTOS = new ArrayList<>();

        List<Task> tasks = taskDao.getAll();

        if (userRole.equals(User.UserRole.Employee.toString())) {
            for (Task task : tasks) {
                if (task.getAssignee() != null) {
                    taskbyEmployeeandStatusDTOS.add(createTaskbyEmployeeandStatusDTO(task));
                }
            }
        } else if (userRole.equals(User.UserRole.Manager.toString())) {
            for (Task task : tasks) {
                taskbyEmployeeandStatusDTOS.add(createTaskbyEmployeeandStatusDTO(task));
            }
        }

        return taskbyEmployeeandStatusDTOS;
    }

    private TaskDTO createTaskbyEmployeeandStatusDTO(Task task) {
        TaskDTO taskbyEmployeeandStatusDTO = new TaskDTO();
        taskbyEmployeeandStatusDTO.setTitle(task.getTitle());
        taskbyEmployeeandStatusDTO.setDescription(task.getDescription());
        taskbyEmployeeandStatusDTO.setTaskStatus(task.getTaskStatus());
        if(task.getAssignee()!=null)
        {
        taskbyEmployeeandStatusDTO.setAssignee(task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName());}
        else
        {
            taskbyEmployeeandStatusDTO.setAssignee("N/A");
        }
        Instant timestamp = task.getCreatedAt();
        LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        taskbyEmployeeandStatusDTO.setCreatedAt(formattedDateTime);

        taskbyEmployeeandStatusDTO.setCreatedBy(task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName());

        return taskbyEmployeeandStatusDTO;
    }


    @Override
    public List<TaskDTO> viewAssignedTasks(Employee employee) {

        List<TaskDTO> assignedTasks=new ArrayList<>();

        for (Task task : taskDao.getAll()) {
            String assigneeFullName = task.getAssignee() != null
                    ? task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName()
                    : "N/A";

            if (assigneeFullName.equals(employee.getFirstName() + " " + employee.getLastName())) {
                TaskDTO assignTask=new TaskDTO();
                assignTask.setTaskStatus(task.getTaskStatus());
                Instant timestamp = task.getCreatedAt();
                LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = localDateTime.format(formatter);
                assignTask.setCreatedAt(formattedDateTime);
                assignTask.setDescription(task.getDescription());
                assignTask.setTitle(task.getTitle());
                assignTask.setTotal_time(task.getTotal_time());
                assignedTasks.add(assignTask);

            }
        }
        return assignedTasks;


    }

    @Override
    public Task getTaskByTitle(String title) {
        if (title != null) {
            for (Task task : taskDao.getAll()) {
                if (task != null && task.getTitle() != null && task.getTitle().equalsIgnoreCase(title)) {
                    return task;
                }
            }
        }
        return null; // Task not found
    }


    @Override
    public ResponseEntity<String> archiveTask(String title) {
        Task task=getTaskByTitle(title);
        task.setAssigned(false);
        task.setAssignee(null);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
