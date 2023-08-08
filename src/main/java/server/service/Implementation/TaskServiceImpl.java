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
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskDao taskDao;
    private final EmployeeDao employeeDao;
    private final ManagerDao managerDao;
    private final TaskHistoryDao taskHistoryDao;


    @Autowired
    public TaskServiceImpl(TaskDao taskDao, EmployeeDao employeeDao, ManagerDao managerDao, TaskHistoryDao taskHistoryDao) {

        this.taskDao = taskDao;
        this.employeeDao = employeeDao;
        this.managerDao = managerDao;
        this.taskHistoryDao = taskHistoryDao;
    }


    @Override
    public List<Task> getAllTasks() {

        return taskDao.getAll();
    }

    @Override
    public List<TaskDTO> viewAllTasksCreatedByManager(Manager manager, String employeeName) {
        Optional<Employee> optionalEmployee = employeeDao.getEmployeeByName(employeeName);

        if (optionalEmployee.isPresent()) {

            Employee employee = optionalEmployee.get();

            List<TaskDTO> taskByEmployees = new ArrayList<>();
            List<Task> tasks = taskDao.getAllTasksByManager(manager, employee);

            for (Task task : tasks) {

                String creatorName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();
                String assigneeName = "N/A"; // Default value in case assignee is null
                if (task.getAssignee() != null) {
                    assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
                }
                Instant timestamp = task.getCreatedAt();
                LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = localDateTime.format(formatter);
                TaskDTO taskByEmployee = new TaskDTO();
                taskByEmployee.setTitle(task.getTitle());
                taskByEmployee.setDescription(task.getDescription());
                taskByEmployee.setAssignee(assigneeName);
                taskByEmployee.setCreatedAt(formattedDateTime);
                taskByEmployee.setCreatedBy(creatorName);

                taskByEmployees.add(taskByEmployee);
            }

            return taskByEmployees;
        } else {

            return null;
        }
    }


    @Override
    public List<TaskDTO> viewAllTasksByUser(String employeeName) {

        Optional<Employee> optionalEmployee = employeeDao.getEmployeeByName(employeeName);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            List<TaskDTO> taskByEmployees = new ArrayList<>();
            List<Task> tasks = taskDao.getAllTasksByEmployee(employee);

            for (Task task : tasks) {
                String assigneeName = "N/A"; // Default value in case assignee is null
                if (task.getAssignee() != null) {
                    assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
                }

                String creatorName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();
                Instant timestamp = task.getCreatedAt();
                LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = localDateTime.format(formatter);
                TaskDTO taskByEmployee = new TaskDTO();


                taskByEmployee.setTitle(task.getTitle());
                taskByEmployee.setDescription(task.getDescription());
                taskByEmployee.setAssignee(assigneeName);
                taskByEmployee.setCreatedAt(formattedDateTime);
                taskByEmployee.setCreatedBy(creatorName);
                taskByEmployee.setTaskStatus(task.getTaskStatus());
                taskByEmployee.setTotal_time(task.getTotal_time());

                taskByEmployees.add(taskByEmployee);

            }
            return taskByEmployees;
        } else {

            return null;
        }
    }


    @Override
    public ResponseEntity<String> createTask(Manager activeManager, String title, String description, double total_time) {
        Task task = new Task(title, description, total_time);
        task.setCreatedBy(activeManager);
        Instant currentInstant = Instant.now();
        task.setCreatedAt(currentInstant); // Store the formatted timestamp
        if (taskDao.isTaskExist(task)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            taskDao.addTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }
    }

    @Override
    public ResponseEntity<String> changeTaskStatus(String title, Task.Status status, User person) {
        Optional<Task> optionalTask = taskDao.getTaskByTitle(title);

        if (optionalTask.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            Task task = optionalTask.get();
            Task.Status currentStatus = task.getTaskStatus();

            if (currentStatus == Task.Status.COMPLETED) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            if (task.getAssignee() == null) {
                throw new ForbiddenAccessException();
            }

            String assigneeUserName = task.getAssignee().getUsername();
            String personUserName = person.getUsername();
            String createdUserName = task.getCreatedBy().getUsername();

            boolean isValidChange = false;

            switch (person.getUserRole()) {
                case "Employee" -> {
                    if (!assigneeUserName.equals(personUserName)) {
                        throw new ForbiddenAccessException();
                    } else if (status == Task.Status.IN_REVIEW && currentStatus == Task.Status.IN_PROGRESS) {
                        Instant endTime = Instant.now();
                        Instant startInstant = task.getStartTime().atZone(ZoneId.systemDefault()).toInstant();
                        Instant endInstant = endTime.atZone(ZoneId.systemDefault()).toInstant();
                        Duration duration = Duration.between(startInstant, endInstant);
                        long minutes = duration.toMinutes();

                        if (minutes >= task.getTotal_time()) {
                            isValidChange = true;
                        }

                    } else if (status == Task.Status.IN_PROGRESS && currentStatus == Task.Status.CREATED) {
                        task.setStartTime(Instant.now());
                        isValidChange = true;
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                }
                case "Manager" -> {
                    if (!createdUserName.equals(personUserName)) {
                        throw new ForbiddenAccessException();
                    } else if (status == Task.Status.COMPLETED && currentStatus == Task.Status.IN_REVIEW) {
                        isValidChange = true;
                    } else {
                        throw new ForbiddenAccessException();
                    }
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
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
                taskHistoryDao.setTaskHistory(history, task);

                return ResponseEntity.status(HttpStatus.OK).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
    }

    @Override
    public List<TaskDTO> viewAllTasksCreatedByManager(Manager activeManager, Task.Status status) {

        List<Task> tasks = taskDao.getTasksByStatus(activeManager, status);
        List<TaskDTO> taskInfoList = new ArrayList<>();

        for (Task task : tasks) {

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

        return taskInfoList;
    }

    @Override
    public List<TaskDTO> viewAllTasksCreatedByManager(Manager activeManager, Task.Status status, String employeeName) {

        Optional<Employee> optionalEmployee = employeeDao.getEmployeeByName(employeeName);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            List<TaskDTO> taskByEmployeeAndStatuses = new ArrayList<>();

            for (Task task : taskDao.getAllTasksByManager(activeManager, employee, status)) {
                String assigneeName = "N/A";

                if (task.getAssignee() != null) {
                    assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
                }
                Instant timestamp = task.getCreatedAt();
                LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = localDateTime.format(formatter);
                TaskDTO taskByEmployeeAndStatus = new TaskDTO();
                taskByEmployeeAndStatus.setTitle(task.getTitle());
                taskByEmployeeAndStatus.setDescription(task.getDescription());
                taskByEmployeeAndStatus.setAssignee(assigneeName);
                taskByEmployeeAndStatus.setTaskStatus(task.getTaskStatus());
                taskByEmployeeAndStatus.setCreatedAt(formattedDateTime);

                taskByEmployeeAndStatuses.add(taskByEmployeeAndStatus);
            }
            return (taskByEmployeeAndStatuses);
        } else {

            return null;
        }
    }


    @Override
    public List<TaskDTO> viewTasksByStatus(Employee employee) {
        List<TaskDTO> allTasks = new ArrayList<>();
        List<Task> tasks = taskDao.getAllTasksByEmployee(employee);

        for (Task task : tasks) {

            allTasks.add(createTaskDTO(task));
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
    public ResponseEntity<String> assignTask(String title, String fullName, Manager manager) {
        Optional<Task> optionalTask = taskDao.getTaskByTitle(title);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            String assignerName = manager.getFirstName() + " " + manager.getLastName();
            if (!(assignerName.equals(task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName()))) {
                throw new ForbiddenAccessException();

            }

            Optional<Manager> optionalManager = managerDao.getManagerByName(fullName);

            if (optionalManager.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            Optional<Employee> optionalEmployee = employeeDao.getEmployeeByName(fullName);
            if (optionalEmployee.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (task.isAssigned()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                task.setAssignee(optionalEmployee.get());
                task.setAssigned(true);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @Override
    public List<TaskDTO> viewTasksByUser(String userRole) {
        List<TaskDTO> taskByEmployeeAndStatusDTOS = new ArrayList<>();

        List<Task> tasks = taskDao.getAllTasksByUserRole(userRole);

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
        LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        taskByEmployeeAndStatusDTO.setCreatedAt(formattedDateTime);

        taskByEmployeeAndStatusDTO.setCreatedBy(task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName());

        return taskByEmployeeAndStatusDTO;
    }


    @Override
    public List<TaskDTO> viewAssignedTasks(Employee employee) {

        List<TaskDTO> assignedTasks = new ArrayList<>();
        List<Task> tasks = taskDao.getAllTasksByEmployee(employee);
        for (Task task : tasks) {
            TaskDTO assignTask = new TaskDTO();
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

        return assignedTasks;


    }

    @Override
    public ResponseEntity<String> archiveTask(String title) {
        Optional<Task> optionalTask = taskDao.getTaskByTitle(title);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setAssigned(false);
            task.setAssignee(null);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


    }
}
