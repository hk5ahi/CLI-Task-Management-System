package server.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.dao.TaskDao;

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


    @Autowired
    public TaskServiceImpl(EmployeeService employeeService, ManagerService managerService, TaskDao taskDao, EmployeeDao employeeDao, ManagerDao managerDao) {
        this.employeeService = employeeService;
        this.managerService = managerService;
        this.taskDao = taskDao;
        this.employeeDao = employeeDao;
        this.managerDao = managerDao;
    }


    @Override
    public List<Task> getallTasks() {

        List<Task> allTasks = taskDao.getAllTasksbyEmployee();

        return allTasks;
    }

    @Override
    public List<TaskbyEmployeeDTO> viewAllTasksbyManager(Manager manager) {
        List<TaskbyEmployeeDTO> taskbyEmployees = new ArrayList<>();
        List<Task> allTasks = taskDao.getAllTasksbyEmployee();

        String managerName = manager.getFirstName() + " " + manager.getLastName();
        for (Task task : allTasks) {
            String assigneeName = "N/A"; // Default value in case assignee is null
            if (task.getAssignee() != null) {
                assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
            }

            String creatorName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();

            if (managerName.equals(creatorName)) {

                Instant timestamp = task.getCreatedAt();
                LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = localDateTime.format(formatter);
                TaskbyEmployeeDTO taskbyEmployee = new TaskbyEmployeeDTO(
                        task.getTitle(),
                        task.getDescription(),
                        assigneeName,
                        formattedDateTime,
                        creatorName
                );

                taskbyEmployees.add(taskbyEmployee);
            }
        }
        return taskbyEmployees;
    }

    @Override
    public List<TaskbyEmployeeDTO> viewAllTasksbySupervisor() {
        List<TaskbyEmployeeDTO> taskbyEmployees = new ArrayList<>();
        List<Task> allTasks = taskDao.getAllTasksbyEmployee();

        for (Task task : allTasks) {
            String assigneeName = "N/A"; // Default value in case assignee is null
            if (task.getAssignee() != null) {
                assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
            }

            String creatorName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();
                Instant timestamp = task.getCreatedAt();
                LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = localDateTime.format(formatter);
                TaskbyEmployeeDTO taskbyEmployee = new TaskbyEmployeeDTO(
                        task.getTitle(),
                        task.getDescription(),
                        assigneeName,
                        formattedDateTime,
                        creatorName
                );

                taskbyEmployees.add(taskbyEmployee);
            }
        return taskbyEmployees;
    }


    @Override
    public String createTask(Manager activeManager, String title, String description, double total_time) {
        Task task = new Task(title, description, total_time);
        task.setCreatedBy(activeManager);
        Instant currentInstant = Instant.now();
        task.setCreatedAt(currentInstant); // Store the formatted timestamp
        for(Task taskinDoa:taskDao.getAllTasksbyEmployee())
        {
            if(taskinDoa.getTitle().equals(task.getTitle()))
            {
                return "Task with that title already exists.";
            }

        }
        taskDao.addTask(task);
        return "Task created successfully";

    }

    @Override
    public String changeTaskStatus(String title, Task.Status status, User person) {
        Task task = getTaskByTitle(title);
        if (task != null) {
            Task.Status currentStatus = task.getTaskStatus();
            boolean isValidChange = false;

            if (task.getAssignee() == null) {
                throw new ForbiddenAccessException();
            }

            if (currentStatus.toString().equals(Task.Status.COMPLETED.toString())) {
                return "The task is completed already.";
            }

            String assigneeFullName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
            String personFullName = person.getFirstName() + " " + person.getLastName();
            String createdFullName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();

            switch (person.getUserRole()) {
                case "Employee" -> {
                    if (assigneeFullName.equals(personFullName)) {
                        if (status == Task.Status.IN_REVIEW) {
                            if (currentStatus.toString().equals(Task.Status.IN_PROGRESS.toString())) {
                                Instant endTime = Instant.now();
                                Instant startInstant = task.getStartTime().atZone(ZoneId.systemDefault()).toInstant();
                                Instant endInstant = endTime.atZone(ZoneId.systemDefault()).toInstant();
                                Duration duration = Duration.between(startInstant, endInstant);
                                long minutes = duration.toMinutes();

                                if (minutes >= task.getTotal_time()) {
                                    isValidChange = true;
                                } else {
                                    long remainingTime = (long) (task.getTotal_time() - minutes);
                                    return "The minimum time for the task to stay in the IN_PROGRESS state is " + task.getTotal_time() + " minutes.\nPlease try after "+remainingTime+" minutes";
                                }
                            } else {
                                return "The task is not in a desirable state.";
                            }
                        } else if (status == Task.Status.IN_PROGRESS) {
                            if (currentStatus.toString().equals(Task.Status.CREATED.toString())) {
                                task.setStartTime(Instant.now());
                                isValidChange = true;
                            } else {
                                return "The task is not in a desirable state.";
                            }
                        } else if (status == Task.Status.COMPLETED) {
                            throw new ForbiddenAccessException();
                        }
                    } else {
                        throw new ForbiddenAccessException();
                    }
                }
                case "Manager" -> {
                    if (createdFullName.equals(personFullName)) {
                        if (currentStatus.toString().equals(Task.Status.IN_REVIEW.toString())) {
                            if (status == Task.Status.COMPLETED) {
                                isValidChange = true;
                            } else {
                                return "The task is not in a desirable state.";
                            }
                        } else {
                            throw new ForbiddenAccessException();
                        }
                    } else {
                        throw new ForbiddenAccessException();
                    }
                }
                default -> {
                    return "Invalid role.";
                }
            }

            if (isValidChange) {
                task.setTaskStatus(status);
                TaskHistory history = new TaskHistory();
                history.setTimestamp(Instant.now());
                history.setOldStatus(currentStatus);
                history.setNewStatus(status);
                task.setHistory(history);

                User user = new User();
                user.setFirstName(person.getFirstName());
                user.setLastName(person.getLastName());
                task.getHistory().setMovedBy(user);

                return "The task has been moved successfully to " + status + " state.";
            } else {
                return "Error";
            }
        } else {
            return "There is no Task";
        }
    }


    @Override
    public List<TaskInfoDTO> viewAllTasksByStatusCreatedBySingleManager(Manager activeManager) {
        String managerName = activeManager.getFirstName() + " " + activeManager.getLastName();
        List<Task> tasksCreatedByManager = taskDao.getAllTasksbyEmployee();
        List<TaskInfoDTO> taskInfoList = new ArrayList<>();

        for (Task task : tasksCreatedByManager) {
            String createdBy = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();

            if (createdBy.equals(managerName)) {
                TaskInfoDTO taskInfo = new TaskInfoDTO();
                taskInfo.setTitle(task.getTitle());
                taskInfo.setDescription(task.getDescription());
                taskInfo.setTaskStatus(task.getTaskStatus().toString());
                Instant timestamp = task.getCreatedAt();
                LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                // Define the desired date and time format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // Format the LocalDateTime to the desired format
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
    public List<TaskbyEmployeeandStatusDTO>viewAllTasksByEmployeeAndStatusCreatedBySingleManager(Manager activeManager) {
        String managerName = activeManager.getFirstName() + " " + activeManager.getLastName();

        List<TaskbyEmployeeandStatusDTO> taskbyEmployeeandStatuses=new ArrayList<>();

        for (Task task : taskDao.getAllTasksbyEmployee()) {
            String createdBy = "N/A"; // Default value in case createdBy is null
            String assigneeName = "N/A"; // Default value in case assignee is null

            if (task.getCreatedBy() != null) {
                createdBy = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();
            }

            if (task.getAssignee() != null) {
                assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
            }

            if (createdBy.equals(managerName)) {

                Instant timestamp = task.getCreatedAt();
                LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                // Define the desired date and time format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // Format the LocalDateTime to the desired format
                String formattedDateTime = localDateTime.format(formatter);
                TaskbyEmployeeandStatusDTO taskbyEmployeeandStatus = new TaskbyEmployeeandStatusDTO(
                        task.getTitle(),
                        task.getDescription(),
                        assigneeName,
                        task.getTaskStatus().toString(),
                        formattedDateTime);


                taskbyEmployeeandStatuses.add(taskbyEmployeeandStatus);


            }
        }

        return (taskbyEmployeeandStatuses);
    }


    @Override
    public List<TaskbyStatusDTO> viewTasksByStatus(Employee employee) {
        List<Task> tasks = taskDao.getAllTasksbyEmployee();
        List<TaskbyStatusDTO> alltasks = new ArrayList<>();
        if (tasks.isEmpty()) {
            return null;
        }

        for (Task task : tasks) {
            if (task.getAssignee() != null) { // Check if the assignee is not null
                String assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
                String employeename = employee.getFirstName() + " " + employee.getLastName();
                if (assigneeName.equals(employeename)) {
                    TaskbyStatusDTO taskbyStatusDTO = new TaskbyStatusDTO();
                    taskbyStatusDTO.setTaskStatus(task.getTaskStatus().toString());
                    taskbyStatusDTO.setDescription(task.getDescription());
                    taskbyStatusDTO.setTitle(task.getTitle());
                    Instant timestamp = task.getCreatedAt();
                    LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    String formattedDateTime = localDateTime.format(formatter);
                    taskbyStatusDTO.setCreatedAt(formattedDateTime);
                    alltasks.add(taskbyStatusDTO);
                }
            }
        }
        return alltasks;
    }




    @Override
    public List<TaskbyStatusDTO> viewallTasksByStatus() {
        List<Task> tasks = taskDao.getAllTasksbyEmployee();
        List<TaskbyStatusDTO> alltasks = new ArrayList<>();
        if (tasks.isEmpty()) {
            return null;
        }

        for (Task task : tasks) {
            if (task.getAssignee() != null) { // Check if the assignee is not null

                    TaskbyStatusDTO taskbyStatusDTO = new TaskbyStatusDTO();
                    taskbyStatusDTO.setTaskStatus(task.getTaskStatus().toString());
                    taskbyStatusDTO.setDescription(task.getDescription());
                    taskbyStatusDTO.setTitle(task.getTitle());
                    Instant timestamp = task.getCreatedAt();
                    LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    String formattedDateTime = localDateTime.format(formatter);
                    taskbyStatusDTO.setCreatedAt(formattedDateTime);
                    alltasks.add(taskbyStatusDTO);
                }
        }
        return alltasks;
    }



    @Override
    public String assignTask(String title, String fullName,Manager manager) {
        Task task = getTaskByTitle(title);
        String assignerName=manager.getFirstName()+" "+manager.getLastName();
        if(!(assignerName.equals(task.getCreatedBy().getFirstName()+" "+task.getCreatedBy().getLastName())))
        {
            throw new ForbiddenAccessException();

        }
        if (task == null) {
            return "Task not found";
        }
        Manager managerByName = (Manager) managerService.getManagerByName(fullName);

        if (managerByName != null) {
            return "The task cannot be assigned to a manager.";
        }
        User assignee = employeeService.getEmployeeByName(fullName);
        if (assignee == null) {
            return "Employee not found.";
        }

        if (task.isAssigned()) {
            return "Task is already assigned.";
        } else {
            task.setAssignee(assignee);
            task.setAssigned(true);
            return "The Task titled " + title + " is assigned to " + assignee.getFirstName() + " " + assignee.getLastName();
        }
    }


    @Override
    public List<TaskbyEmployeeandStatusDTO> viewTasksByUser(String userRole) {
        List<TaskbyEmployeeandStatusDTO> taskbyEmployeeandStatusDTOS = new ArrayList<>();

        List<Task> tasks = taskDao.getAllTasksbyEmployee();

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

    private TaskbyEmployeeandStatusDTO createTaskbyEmployeeandStatusDTO(Task task) {
        TaskbyEmployeeandStatusDTO taskbyEmployeeandStatusDTO = new TaskbyEmployeeandStatusDTO();
        taskbyEmployeeandStatusDTO.setTitle(task.getTitle());
        taskbyEmployeeandStatusDTO.setDescription(task.getDescription());
        taskbyEmployeeandStatusDTO.setTaskStatus(task.getTaskStatus().toString());
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
    public List<ViewAssignTask> viewAssignedTasks(Employee employee) {

        List<ViewAssignTask> assignedTasks=new ArrayList<>();

        for (Task task : taskDao.getAllTasksbyEmployee()) {
            String assigneeFullName = task.getAssignee() != null
                    ? task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName()
                    : "N/A";

            if (assigneeFullName.equals(employee.getFirstName() + " " + employee.getLastName())) {
                ViewAssignTask assignTask=new ViewAssignTask();
                assignTask.setTaskStatus(task.getTaskStatus().toString());
                Instant timestamp = task.getCreatedAt();
                LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = localDateTime.format(formatter);
                assignTask.setCreatedAt(formattedDateTime);
                assignTask.setDescription(task.getDescription());
                assignTask.setTitle(task.getTitle());
                assignedTasks.add(assignTask);

            }
        }
        return assignedTasks;


    }

    @Override
    public Task getTaskByTitle(String title) {
        if (title != null) {
            for (Task task : taskDao.getAllTasksbyEmployee()) {
                if (task != null && task.getTitle() != null && task.getTitle().equalsIgnoreCase(title)) {
                    return task;
                }
            }
        }
        return null; // Task not found
    }


    @Override
    public String archiveTask(String title) {
        Task task=getTaskByTitle(title);
        task.setAssigned(false);
        task.setAssignee(null);

        return "The task has been archived successfully.";
    }


}
