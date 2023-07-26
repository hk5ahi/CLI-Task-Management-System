package server.service.Implementation;

import server.dao.implementation.EmployeeDaoImpl;
import server.dao.implementation.ManagerDaoImpl;
import server.dao.implementation.TaskDaoImpl;
import server.domain.*;
import server.service.EmployeeService;
import server.service.ManagerService;
import server.service.TaskService;
import server.utilities.Taskbytitle;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TaskServiceImpl implements TaskService {

    EmployeeService employeeService = new EmployeeServiceImpl();
    private TaskDaoImpl taskDao = TaskDaoImpl.getInstance();


    ManagerService managerService = new ManagerServiceImpl();
    private EmployeeDaoImpl employeeDao = EmployeeDaoImpl.getInstance();

    private ManagerDaoImpl managerDao = ManagerDaoImpl.getInstance();

    @Override
    public void viewAllTasks() {
        List<Task> allTasks = taskDao.getAllTasksbyEmployee();
        if (allTasks.isEmpty()) {
            System.out.println("There are no tasks yet.");
            return;
        }

        System.out.println("The Tasks of the system are: ");

        for (Task task : allTasks) {
            String assigneeName = "N/A"; // Default value in case assignee is null
            if (task.getAssignee() != null) {
                assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
            }

            String creatorName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();

            System.out.printf("The title of task is %s with its description which is %s whose employee is %s and its status is %s. It is created by %s.%n",
                    task.getTitle(), task.getDescription(), assigneeName, task.getTaskStatus(), creatorName);
        }
    }


    @Override
    public void createTask(Manager activeManager, String title, String description, int total_time) {
        Task task = new Task(title, description, total_time);
        task.setCreatedBy(activeManager);
        task.setCreatedAt(Instant.now().toString());
        taskDao.addTask(task);
        System.out.println("The Task has been created Successfully.");
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
            case "Employee":
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
                break;

            case "Manager":

                if (createdFullName.equals(personFullName)) {
                    if (currentStatus.equals(Task.Status.IN_REVIEW.toString())) {
                        isValidChange = true;
                    } else {
                        System.out.println("The task is not in a desirable state.");
                    }
                } else {

                    System.out.println("You do not have permission to do this.");
                }
                break;

            default:
                System.out.println("Invalid role.");
                break;
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
    public void viewAllTasksByStatusCreatedBySingleManager(Manager activeManager) {
        String managerName = activeManager.getFirstName() + " " + activeManager.getLastName();
        List<Task> tasksCreatedByManager = new ArrayList<>();

        for (Task task : taskDao.getAllTasksbyEmployee()) {
            String createdBy = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();

            if (createdBy.equals(managerName)) {
                tasksCreatedByManager.add(task);
            }
        }

        if (tasksCreatedByManager.isEmpty()) {
            System.out.println("No tasks yet.");
            return;
        }

        System.out.println("The tasks created by the manager are: ");

        for (Task task : tasksCreatedByManager) {
            System.out.printf("The title of task is %s with its description which is %s and its status is %s.%n",
                    task.getTitle(), task.getDescription(), task.getTaskStatus());
        }
    }


    @Override
    public void viewAllTasksByEmployeeAndStatusCreatedBySingleManager(Manager activeManager) {
        String managerName = activeManager.getFirstName() + " " + activeManager.getLastName();
        List<Task> tasksCreatedByManager = new ArrayList<>();

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
                tasksCreatedByManager.add(task);
            }
        }

        if (tasksCreatedByManager.isEmpty()) {
            System.out.println("No tasks yet.");
            return;
        }

        System.out.println("The tasks created by the manager for each employee are: ");

        for (Task task : tasksCreatedByManager) {
            String assigneeName = "N/A"; // Default value in case assignee is null

            if (task.getAssignee() != null) {
                assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
            }

            System.out.printf("The title of task is %s with its description which is %s whose employee is %s and its status is %s.\n",
                    task.getTitle(), task.getDescription(), assigneeName, task.getTaskStatus());
        }
    }


    @Override
    public void viewTasksByStatus(Employee employee) {
        List<Task> tasks = taskDao.getAllTasksbyEmployee();

        if (tasks.isEmpty()) {
            System.out.println("No Tasks");
            return;
        }

        System.out.println("The tasks whose status are CREATED are:");
        printTasksByStatus(tasks, Task.Status.CREATED, employee);

        System.out.println("\nThe tasks whose status are IN_PROGRESS are:");
        printTasksByStatus(tasks, Task.Status.IN_PROGRESS, employee);

        System.out.println("\nThe tasks whose status are IN_REVIEW are:");
        printTasksByStatus(tasks, Task.Status.IN_REVIEW, employee);

        System.out.println("\nThe tasks whose status are COMPLETED are:");
        printTasksByStatus(tasks, Task.Status.COMPLETED, employee);
    }

    @Override
    public void printTasksByStatus(List<Task> tasks, Task.Status status, Employee employee) {
        boolean hasTasksWithStatus = false;

        for (Task task : tasks) {
            String assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
            String employeename = employee.getFirstName() + " " + employee.getLastName();
            if ((task.getTaskStatus().equals(status.toString()) && assigneeName.equals(employeename))) {
                System.out.printf("The title of task is %s with its description which is %s.\n", task.getTitle(), task.getDescription());
                hasTasksWithStatus = true;
            }
        }

        if (!hasTasksWithStatus) {
            System.out.println("There are no tasks yet.");
        }
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
    public void assignTask() {
        Taskbytitle taskbytitle = new Taskbytitle();
        Task task = taskbytitle.gettaskbytitle();

        if (task == null) {
            System.out.println("Task not found.");
            return;
        }

        String taskTitle = task.getTitle();

        System.out.println("The Employees are:");
        for (Employee employee : employeeDao.getEmployees()) {
            System.out.println(employee.getFirstName() + " " + employee.getLastName());
        }

        User assignee = null;

        do {
            String name = taskbytitle.nameofEmployee();
            assignee = employeeService.getEmployeeByName(name);
            Manager manager = (Manager) managerService.getManagerByName(name);

            if (manager != null) {
                System.out.println("The task cannot be assigned to a manager.");
            } else if (assignee != null) {
                // If the name is valid and not the manager's name, proceed with task assignment
                break;
            } else {
                // If the name is not found, display a message and re-loop to get a different name
                System.out.println("Employee not found.");
            }
        } while (true);

        if (assignee != null) {
            if (task.isAssigned()) {
                System.out.println("Task is already assigned.");
            } else {
                task.setAssignee(assignee);
                task.setAssigned(true);
                System.out.printf("The Task titled %s is assigned to %s.\n", taskTitle, assignee.getFirstName() + " " + assignee.getLastName());
            }
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
    public void viewAssignedTasks(Employee employee) {
        boolean hasAssignedTasks = false;

        System.out.println("The assigned tasks for the employee are:");

        for (Task task : taskDao.getAllTasksbyEmployee()) {
            String assigneeFullName = task.getAssignee() != null
                    ? task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName()
                    : "N/A";

            if (assigneeFullName.equals(employee.getFirstName() + " " + employee.getLastName())) {
                System.out.println(task.getTitle());
                hasAssignedTasks = true;
            }
        }

        if (!hasAssignedTasks) {
            System.out.println("No Tasks.");
        }
    }


    @Override
    public Task getTaskByTitle(String title) {
        for (Task task : taskDao.getAllTasksbyEmployee()) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null; // Task not found
    }



    @Override
    public void archiveTask() {
        Taskbytitle taskbytitle = new Taskbytitle();
        Task task = taskbytitle.gettaskbytitle();
        task.setAssigned(false);
        task.setAssignee(null);

        System.out.println("The task has been archived successfully.");
    }


}
