package server.dao.implementation;
import org.springframework.stereotype.Repository;
import server.dao.TaskDao;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TaskDaoImpl implements TaskDao {

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        this.tasks.add(task);
    }


    @Override
    public List<Task> getAll() {
        return this.tasks;
    }

    @Override
    public boolean isTaskExist(Task task) {
        return tasks.stream().anyMatch(givenTask -> givenTask.getTitle().equals(task.getTitle()));
    }


    @Override
    public Optional<Task> getTaskByTitle(String title) {
        if (title != null) {
            for (Task task : tasks) {
                if (task != null && task.getTitle() != null && task.getTitle().equalsIgnoreCase(title)) {
                    return Optional.of(task);
                }
            }
        }
        return Optional.empty(); // Task not found
    }


    @Override
    public List<Task> getAllTasksByManager(Manager manager, Employee employee) {
        return tasks.stream()
                .filter(task -> {
                    String assigneeName = task.getAssignee() != null ? task.getAssignee().getUsername() : "N/A";
                    String creatorUserName = task.getCreatedBy().getUsername();
                    return manager.getUsername().equals(creatorUserName) && employee.getUsername().equals(assigneeName);
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<Task> getAllTasksByManager(Manager manager, Employee employee, Task.Status status) {
        return tasks.stream()
                .filter(task -> {
                    String createdUserName = task.getCreatedBy() != null ? task.getCreatedBy().getUsername() : "N/A";
                    String assigneeUserName = task.getAssignee() != null ? task.getAssignee().getUsername() : "N/A";
                    return manager.getUsername().equals(createdUserName)
                            && employee.getUsername().equals(assigneeUserName)
                            && task.getTaskStatus().equals(status);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasksByStatus(Manager manager, Task.Status status) {
        return tasks.stream()
                .filter(task -> task.getCreatedBy().getUsername().equals(manager.getUsername())
                        && task.getTaskStatus().equals(status))
                .collect(Collectors.toList());
    }


    @Override
    public List<Task> getAllTasksByUserRole(User.UserRole userRole) {
        if (userRole.equals(User.UserRole.Employee)) {
            return tasks.stream()
                    .filter(task -> task.getAssignee() != null)
                    .collect(Collectors.toList());
        } else if (userRole.equals(User.UserRole.Manager)) {
            return tasks;
        } else {
            return Collections.emptyList(); // Return an empty list instead of null
        }
    }

    @Override
    public List<Task> getAllTasksByEmployee(Employee employee) {
        return tasks.stream()
                .filter(task -> {
                    String assigneeName = task.getAssignee() != null ? task.getAssignee().getUsername() : "N/A";
                    return employee.getUsername().equals(assigneeName);
                })
                .collect(Collectors.toList());
    }

}