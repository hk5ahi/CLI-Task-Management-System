package server.service.Implementation;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskServiceImpl implements TaskService {

    @Override
    public Task createTask(String title, String description, int total_time) {
        return new Task(title, description, total_time);
    }

    @Override
    public List<Task> getAllTasks() {
        return Employee.allTasks;
    }

    @Override
    public Task getTaskByTitle(String title) {
        for (Task task : Employee.allTasks) {
            if (task.title.equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null;
    }

    @Override
    public void assignTask(Task task, String assignee) {
        if (!task.assigned) {
            task.assignee = assignee;
            task.assigned = true;

            // Add the task to the assigned tasks list of the selected employee
            for (Employee employee : Employee.employees) {
                String name = employee.first_Name + " " + employee.last_Name;
                if (name.equalsIgnoreCase(assignee)) {
                    employee.assignedTasks.add(task);
                    break;
                }
            }
        } else {
            System.out.println("Task is already assigned.");
        }
    }

    @Override
    public void changeTaskStatus(Task task, Task.Status status) {
        if (task.taskStatus.equals(String.valueOf(status))) {
            System.out.println("The task is already in the desired status.");
        } else {
            // Check if the status transition is valid based on business rules (e.g., CREATED to IN_PROGRESS)
            boolean isValidTransition = false;
            switch (status) {
                case IN_PROGRESS:
                    if (task.taskStatus.equals(String.valueOf(Task.Status.CREATED))) {
                        // Implement additional validation rules if needed
                        isValidTransition = true;
                    }
                    break;
                case IN_REVIEW:
                    if (task.taskStatus.equals(String.valueOf(Task.Status.IN_PROGRESS))) {
                        // Implement additional validation rules if needed
                        isValidTransition = true;
                    }
                    break;
                case COMPLETED:
                    if (task.taskStatus.equals(String.valueOf(Task.Status.IN_REVIEW))) {
                        // Implement additional validation rules if needed
                        isValidTransition = true;
                    }
                    break;
                default:
                    // Other transitions are not allowed
                    break;
            }

            if (isValidTransition) {
                task.history = new TaskHistory();
                task.history.timestamp = LocalDateTime.now();
                task.history.old_status = task.taskStatus;
                task.history.new_status = String.valueOf(status);
                task.history.moved_by = task.createdBy;
                task.taskStatus = String.valueOf(status);
            } else {
                System.out.println("Invalid status transition.");
            }
        }
    }

    @Override
    public void archiveTask(Task task) {
        if (task.assigned) {
            task.assignee = null;
            task.assigned = false;
            task.taskStatus = String.valueOf(Task.Status.CREATED);
        } else {
            System.out.println("The task is not assigned. Archiving is not allowed.");
        }
    }

    @Override
    public void addComment(Task task, String message) {
        Comment comment = new Comment();
        comment.body = message;
        comment.createdAt = LocalDateTime.now().toString();
        comment.createdBy = task.createdBy;
        task.comments.add(comment);
    }

    @Override
    public List<Comment> getCommentsForTask(Task task) {
        return task.comments;
    }

    @Override
    public List<TaskHistory> getTaskHistory(Task task) {
        List<TaskHistory> taskHistoryList = new ArrayList<>();
        if (task.history != null) {
            taskHistoryList.add(task.history);
        }
        return taskHistoryList;
    }
}
