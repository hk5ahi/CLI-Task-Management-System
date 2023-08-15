package server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;

import java.util.List;
import java.util.Optional;

public interface TaskDao extends JpaRepository<Task,String> {

//    List<Task> getAll();

//    void addTask(Task allTasks);

    List<Task> getTasksByCreatedByUsernameAndAssignee_Username(String Username, String Assignee_Username);
    List<Task> getTasksByCreatedByUsernameAndAssignee_UsernameAndTaskStatus(String Username, String Assignee_Username,Task.Status status);

    List<Task> getTasksByAssignee_Username(String Username);

    List<Task> getTasksByAssigneeNotNull();
    List<Task> getTasksByCreatedByUsernameAndTaskStatus(String CreatedBy,Task.Status status);

    boolean existsByTitle(String title);

    Optional<Task> findByTitle(String title);



}
