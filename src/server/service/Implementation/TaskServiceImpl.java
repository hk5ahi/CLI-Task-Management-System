package server.service.Implementation;

import server.dao.implementation.EmployeeDaoImpl;
import server.dao.implementation.ManagerDaoImpl;
import server.dao.implementation.TaskDaoImpl;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.service.EmployeeService;
import server.service.ManagerService;
import server.service.TaskService;
import server.utilities.Taskbytitle;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class TaskServiceImpl implements TaskService {

    EmployeeService employeeService = new EmployeeServiceImpl();
    private TaskDaoImpl taskDao = TaskDaoImpl.getInstance();
    private Taskbytitle taskbytitle = new Taskbytitle();

    ManagerService managerService = new ManagerServiceImpl();
    private EmployeeDaoImpl employeeDao = EmployeeDaoImpl.getInstance();

    private ManagerDaoImpl managerDao = ManagerDaoImpl.getInstance();

    @Override
    public void viewAllTasks() {
        for (Task task : taskDao.getAllTasksbyEmployee()) {
            String assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
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
    }

    @Override
    public void changeTaskStatus(Task task, Task.Status status, User person) {
        String currentStatus = task.getTaskStatus();
        boolean isValidChange = false;

        switch (person.getUserRole()) {
            case "Employee":
                if (status == Task.Status.IN_REVIEW) {
                    if (currentStatus.equals(Task.Status.IN_PROGRESS.toString())) {
                        LocalDateTime endTime = LocalDateTime.now();
                        Duration duration = Duration.between(task.getStartTime(), endTime);
                        long minutes = duration.toMinutes();
                        if (minutes >= task.getTotal_time()) {
                            isValidChange = true;
                        } else {
                            System.out.printf("The minimum time for the task to stay in the IN_PROGRESS state is %d minutes.", task.getTotal_time());
                        }
                    } else {
                        System.out.println("The task is not in a desirable state.");
                    }
                } else if (status == Task.Status.IN_PROGRESS) {
                    if (currentStatus.equals(Task.Status.CREATED.toString())) {
                        isValidChange = true;
                    } else {
                        System.out.println("The task is not in a desirable state.");
                    }
                }
                break;

            case "Manager":
                if (currentStatus.equals(Task.Status.IN_REVIEW.toString())) {
                    isValidChange = true;
                } else {
                    System.out.println("The task is not in a desirable state.");
                }
                break;

            default:
                System.out.println("Invalid role.");
                break;
        }

        if (isValidChange) {
            task.setTaskStatus(status.toString());
            task.getHistory().setTimestamp(Instant.now());
            task.getHistory().setOldStatus(Task.Status.valueOf(currentStatus));
            task.getHistory().setNewStatus(status);

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

        for (Task task : taskDao.getAllTasksbyEmployee()) {
            String createdBy = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();

            if (createdBy.equals(managerName)) {
                System.out.printf("The title of task is %s with its description which is %s and its status is %s.\n",
                        task.getTitle(), task.getDescription(), task.getTaskStatus());
            }
        }
    }


    @Override
    public void viewAllTasksByEmployeeAndStatusCreatedBySingleManager(Manager activeManager) {
        String managerName = activeManager.getFirstName() + " " + activeManager.getLastName();

        for (Task task : taskDao.getAllTasksbyEmployee()) {
            String createdBy = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();
            String assigneeName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();

            if (createdBy.equals(managerName)) {
                System.out.printf("The title of task is %s with its description which is %s whose employee is %s and its status is %s.\n",
                        task.getTitle(), task.getDescription(), assigneeName, task.getTaskStatus());
            }
        }
    }


    @Override
    public void viewTasksByStatus() {
        List<Task> tasks = taskDao.getAllTasksbyEmployee();

        if (tasks.isEmpty()) {
            System.out.println("No Tasks");
            return;
        }

        System.out.println("The tasks whose status are CREATED are:");
        printTasksByStatus(tasks, Task.Status.CREATED);

        System.out.println("\nThe tasks whose status are IN_PROGRESS are:");
        printTasksByStatus(tasks, Task.Status.IN_PROGRESS);

        System.out.println("\nThe tasks whose status are IN_REVIEW are:");
        printTasksByStatus(tasks, Task.Status.IN_REVIEW);

        System.out.println("\nThe tasks whose status are COMPLETED are:");
        printTasksByStatus(tasks, Task.Status.COMPLETED);
    }

    @Override
    public void printTasksByStatus(List<Task> tasks, Task.Status status) {
        for (Task task : tasks) {
            if (task.getTaskStatus().equals(status.toString())) {
                System.out.printf("The title of task is %s with its description which is %s.\n", task.getTitle(), task.getDescription());
            }
        }
    }

    @Override
    public void assignTask() {
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
        if (person.getUserRole().equals("Employee")) {
            System.out.println("The tasks are categorized employee-wise with their respective statuses.");

            for (Employee employee : employeeDao.getEmployees()) {
                System.out.printf("The name of Employee is %s %s and its assigned tasks with their status are:\n",
                        employee.getFirstName(), employee.getLastName());

                for (Task task : taskDao.getAllTasksbyEmployee()) {
                    if (task.getAssignee().equals(employee)) {
                        System.out.printf("The title of task is %s with its description which is %s and its status is %s and is created by %s.\n",
                                task.getTitle(), task.getDescription(), task.getTaskStatus(),
                                task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName());
                    }
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
            if (task.getAssignee().equals(employee)) {
                System.out.println(task.getDescription());
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
        Task task = taskbytitle.gettaskbytitle();
        task.setAssigned(false);
        task.setAssignee(null);

        System.out.println("The task has been archived successfully.");
    }


}
