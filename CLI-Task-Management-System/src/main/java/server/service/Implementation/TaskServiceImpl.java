package server.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.dao.TaskDao;

import server.domain.*;
import server.dto.TaskInfoDTO;
import server.dto.TaskbyEmployeeDTO;
import server.dto.TaskbyEmployeeandStatusDTO;
import server.dto.TaskbyStatusDTO;
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
    public TaskServiceImpl(EmployeeService employeeService, ManagerService managerService, TaskDao taskDao, EmployeeDao employeeDao,ManagerDao managerDao) {
        this.employeeService=employeeService;
        this.managerService=managerService;
        this.taskDao=taskDao;
        this.employeeDao=employeeDao;
        this.managerDao=managerDao;
    }


    @Override
    public List<Task> getallTasks()
    {

        List<Task> allTasks=taskDao.getAllTasksbyEmployee();

            return allTasks;
    }
    @Override
    public List<TaskbyEmployeeDTO> viewAllTasks(Manager manager) {
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
                TaskbyEmployeeDTO taskbyEmployee = new TaskbyEmployeeDTO(
                        task.getTitle(),
                        task.getDescription(),
                        assigneeName


                );

                taskbyEmployees.add(taskbyEmployee);
            }
        }

        return taskbyEmployees;
    }


    @Override
    public void createTask(Manager activeManager, String title, String description, double total_time) {
        Task task = new Task(title, description, total_time);
        task.setCreatedBy(activeManager);
        Instant currentInstant = Instant.now();
        task.setCreatedAt(currentInstant); // Store the formatted timestamp

        taskDao.addTask(task);

    }

    @Override
    public void changeTaskStatus(Task task, Task.Status status, User person) {
        String currentStatus = task.getTaskStatus();
        boolean isValidChange = false;
        if (task.getAssignee() == null) {
            System.out.println("You do not have permission to do this.");
            return;
        }
        String assigneeFullName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
        String personFullName = person.getFirstName() + " " + person.getLastName();
        String createdFullName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();

        switch (person.getUserRole()) {
            case "Employee" -> {
                if (assigneeFullName.equals(personFullName)) {
                    if (status == Task.Status.IN_REVIEW) {
                        if (currentStatus.equals(Task.Status.IN_PROGRESS.toString())) {
                            LocalDateTime endTime = LocalDateTime.now();
                            Instant startInstant = task.getStartTime().atZone(ZoneId.systemDefault()).toInstant();
                            Instant endInstant = endTime.atZone(ZoneId.systemDefault()).toInstant();
                            Duration duration = Duration.between(startInstant, endInstant);
                            long minutes = duration.toMinutes();
                            if (minutes >= task.getTotal_time()) {
                                isValidChange = true;
                            } else {
                                System.out.printf("The minimum time for the task to stay in the IN_PROGRESS state is %f minutes.", task.getTotal_time());
                            }
                        } else {
                            System.out.println("The task is not in a desirable state.");
                        }
                    } else if (status == Task.Status.IN_PROGRESS) {
                        if (currentStatus.equals(Task.Status.CREATED.toString())) {
                            task.setStartTime(Instant.now());
                            isValidChange = true;
                        } else {
                            System.out.println("The task is not in a desirable state.");
                        }
                    }
                } else {

                    System.out.println("You do not have permission to do this.");

                }
            }
            case "Manager" -> {
                if (createdFullName.equals(personFullName)) {
                    if (currentStatus.equals(Task.Status.IN_REVIEW.toString())) {
                        isValidChange = true;
                    } else {
                        System.out.println("The task is not in a desirable state.");
                    }
                } else {

                    System.out.println("You do not have permission to do this.");
                }
            }
            default -> System.out.println("Invalid role.");
        }

        if (isValidChange) {
            task.setTaskStatus(status.toString());
            TaskHistory history = new TaskHistory();
            history.setTimestamp(Instant.now());
            history.setOldStatus(Task.Status.valueOf(currentStatus));
            history.setNewStatus(status);
            task.setHistory(history);

            User user = new User();
            user.setFirstName(person.getFirstName());
            user.setLastName(person.getLastName());
            task.getHistory().setMovedBy(user);

            System.out.println("The task has been moved successfully to " + status + " state.");
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
                taskInfo.setTaskStatus(task.getTaskStatus());
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

                TaskbyEmployeeandStatusDTO taskbyEmployeeandStatus = new TaskbyEmployeeandStatusDTO(
                        task.getTitle(),
                        task.getDescription(),
                        assigneeName,
                        task.getTaskStatus()

                );

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
                    taskbyStatusDTO.setTaskStatus(task.getTaskStatus());
                    taskbyStatusDTO.setDescription(task.getDescription());
                    taskbyStatusDTO.setTitle(task.getTitle());
                    alltasks.add(taskbyStatusDTO);
                }
            }
        }
        return alltasks;
    }




    @Override
    public void viewallTasksByStatus() {
        List<Task> tasks = taskDao.getAllTasksbyEmployee();

        if (tasks.isEmpty()) {
            System.out.println("No Tasks");
            return;
        }

        System.out.println("The tasks whose status are CREATED are:");
        printallTasksByStatus(tasks, Task.Status.CREATED);

        System.out.println("\nThe tasks whose status are IN_PROGRESS are:");
        printallTasksByStatus(tasks, Task.Status.IN_PROGRESS);

        System.out.println("\nThe tasks whose status are IN_REVIEW are:");
        printallTasksByStatus(tasks, Task.Status.IN_REVIEW);

        System.out.println("\nThe tasks whose status are COMPLETED are:");
        printallTasksByStatus(tasks, Task.Status.COMPLETED);
    }

    @Override
    public void printallTasksByStatus(List<Task> tasks, Task.Status status) {
        boolean hasTasksWithStatus = false;

        for (Task task : tasks) {

            if ((task.getTaskStatus().equals(status.toString()))) {
                System.out.printf("The title of task is %s with its description which is %s.\n", task.getTitle(), task.getDescription());
                hasTasksWithStatus = true;
            }
        }

        if (!hasTasksWithStatus) {
            System.out.println("There are no tasks yet.");
        }
    }


    @Override
    public String assignTask(String title, String fullname) {
        Task task = getTaskByTitle(title);

        if (task == null) {
            return "Task not found";
        }
        Manager manager = (Manager) managerService.getManagerByName(fullname);

        if (manager != null) {
            return "The task cannot be assigned to a manager.";
        }
        User assignee = employeeService.getEmployeeByName(fullname);
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
    public void viewTasksByUser(User person) {
        boolean noTasks = false;
        if (person.getUserRole().equals("Employee")) {
            System.out.println("The tasks are categorized employee-wise with their respective statuses.");

            for (Employee employee : employeeDao.getEmployees()) {
                System.out.printf("The name of Employee is %s %s and its assigned tasks with their status are:\n",
                        employee.getFirstName(), employee.getLastName());

                for (Task task : taskDao.getAllTasksbyEmployee()) {
                    if (task.getAssignee() != null && task.getAssignee().equals(employee)) {
                        System.out.printf("The title of task is %s with its description which is %s and its status is %s and is created by %s.\n",
                                task.getTitle(), task.getDescription(), task.getTaskStatus(),
                                task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName());

                        noTasks = true;
                    }
                }
                if (!noTasks) {
                    System.out.println("There are no tasks assigned to the employee.");
                }
            }
        } else {
            System.out.println("The tasks are categorized manager-wise with their respective statuses.");

            for (Manager manager : managerDao.getManagers()) {
                System.out.printf("The name of Manager is %s %s and its created tasks with their status are:\n",
                        manager.getFirstName(), manager.getLastName());

                for (Task task : taskDao.getAllTasksbyEmployee()) {
                    if (task.getCreatedBy().equals(manager)) {
                        System.out.printf("The title of task is %s with its description which is %s and its status is %s.\n",
                                task.getTitle(), task.getDescription(), task.getTaskStatus());
                    }
                }
            }
        }
    }


    @Override
    public List<Task> viewAssignedTasks(Employee employee) {

        List<Task> assignedTasks=new ArrayList<>();
//        System.out.println("The assigned tasks for the employee are:");

        for (Task task : taskDao.getAllTasksbyEmployee()) {
            String assigneeFullName = task.getAssignee() != null
                    ? task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName()
                    : "N/A";

            if (assigneeFullName.equals(employee.getFirstName() + " " + employee.getLastName())) {
                assignedTasks.add(task);

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
    public void archiveTask() {
//        Taskbytitle taskbytitle = new Taskbytitle();
//        Task task = taskbytitle.gettaskbytitle();
//        task.setAssigned(false);
//        task.setAssignee(null);
//
//        System.out.println("The task has been archived successfully.");
    }


}
