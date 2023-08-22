package server.dao.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import server.dao.CustomTaskDao;
import server.domain.Task;
import server.domain.User;
import server.dto.QueryParameterDTO;

import server.utilities.UtilityService;

import java.util.List;
import java.util.Optional;
@Repository
public class CustomTaskDaoImpl implements CustomTaskDao {

    @PersistenceContext
    private final EntityManager entityManager;
    @Autowired
    private final UtilityService utilityService;

    public CustomTaskDaoImpl(EntityManager entityManager, UtilityService utilityService) {
        this.entityManager = entityManager;

        this.utilityService = utilityService;
    }

    @Override
    public List<Task> filterTasksByQueryParameters(QueryParameterDTO queryParams, String header) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM Task t WHERE 1 = 1");

        Optional<User> optionalUser = utilityService.getUser(header);
        User user = optionalUser.orElse(null);

        User.UserRole userRole = queryParams.getByUserRole();
        String employeeName = queryParams.getEmployeeName();
        Task.Status taskStatus = queryParams.getTaskStatus();

        if (userRole == User.UserRole.Supervisor) {
            if (!employeeName.equals("N/A")) {
                jpql.append(" AND t.assignee = :assignee");
            }
            if (taskStatus != null) {
                jpql.append(" AND t.taskStatus = :task_status");
            }
        }

        if (userRole == User.UserRole.Manager) {
            if (!employeeName.equals("N/A")) {
                jpql.append(" AND t.assignee = :assignee AND t.createdBy = :manager");
            }
            if (taskStatus != null) {
                jpql.append(" AND t.taskStatus = :task_status AND t.createdBy = :manager");
            }
        }

        if (userRole == User.UserRole.Employee && queryParams.isByAssigned()) {
            jpql.append(" AND t.assignee = :assignee");
        }

        TypedQuery<Task> query = entityManager.createQuery(jpql.toString(), Task.class);

        if (!employeeName.equals("N/A")) {
            query.setParameter("assignee", utilityService.getAssigneeByName(employeeName));
        }
        if (taskStatus != null) {
            query.setParameter("task_status", taskStatus);
        }
        if (userRole == User.UserRole.Manager || userRole == User.UserRole.Employee) {
            query.setParameter("manager", user);
            query.setParameter("assignee", user);
        }

        return query.getResultList();
    }

}
