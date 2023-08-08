package server.dao.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import server.dao.TaskDao;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public boolean isTaskExist(Task task)
    {
        for(Task givenTask:tasks)
        {
            if(givenTask.getTitle().equals(task.getTitle()))
            {

                return true;
            }

        }
        return false;

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
    public List<Task> getAllTasksByManager(Manager manager, Employee employee)
    {
        List<Task> returnedTasks=new ArrayList<>();
        for(Task task:tasks)
        {
            String assigneeName = "N/A"; // Default value in case assignee is null
            if (task.getAssignee() != null) {
                assigneeName = task.getAssignee().getUsername();
            }
            String creatorUserName = task.getCreatedBy().getUsername();
            if (manager.getUsername().equals(creatorUserName) && employee.getUsername().equals(assigneeName))
            {
                returnedTasks.add(task);
            }
        }
        return  returnedTasks;

    }

    @Override
    public List<Task> getAllTasksByManager(Manager manager, Employee employee,Task.Status status)
    {
        List<Task> returnedTasks=new ArrayList<>();
        for(Task task:tasks)
        {
            String createdUserName = "N/A"; // Default value in case createdBy is null
            String assigneeUserName = "N/A"; // Default value in case assignee is null

            if (task.getCreatedBy() != null) {
                createdUserName = task.getCreatedBy().getUsername();
            }

            if (task.getAssignee() != null) {
                assigneeUserName = task.getAssignee().getUsername();
            }
            if (manager.getUsername().equals(createdUserName) && employee.getUsername().equals(assigneeUserName) && (task.getTaskStatus().equals(status)))
            {
                returnedTasks.add(task);
            }
        }
        return  returnedTasks;

    }
    @Override
    public List<Task> getTasksByStatus(Manager manager,Task.Status status)
    {

        List<Task> returnedTasks=new ArrayList<>();
        for(Task task:tasks)
        {
            String createdBy = task.getCreatedBy().getUsername();

            if (createdBy.equals(manager.getUsername()) && task.getTaskStatus().equals(status))
            {
                returnedTasks.add(task);
            }
        }
        return  returnedTasks;



    }

    @Override
    public List<Task> getAllTasksByUserRole(String userRole)

    {
        List<Task> returnedTasks=new ArrayList<>();
        if(userRole.equals(User.UserRole.Employee.toString()))
        {
            for(Task task:tasks)
            {
                if (task.getAssignee() != null) {

                    returnedTasks.add(task);
                }
            }
            return returnedTasks;

        } else if (userRole.equals(User.UserRole.Manager.toString())) {

                return tasks;
        }
        else
        {
            return null;
        }

    }
    @Override
    public List<Task> getAllTasksByEmployee( Employee employee)
    {
        List<Task> returnedTasks=new ArrayList<>();
        for(Task task:tasks)
        {
            String assigneeName = "N/A"; // Default value in case assignee is null
            if (task.getAssignee() != null) {
                assigneeName = task.getAssignee().getUsername();
            }

            if (employee.getUsername().equals(assigneeName))
            {
                returnedTasks.add(task);
            }
        }
        return  returnedTasks;

    }

}
