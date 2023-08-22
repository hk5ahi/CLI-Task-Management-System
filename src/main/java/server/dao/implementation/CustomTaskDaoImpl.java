package server.dao.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

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

    private final UtilityService utilityService;

    public CustomTaskDaoImpl(EntityManager entityManager, UtilityService utilityService) {
        this.entityManager = entityManager;

        this.utilityService = utilityService;
    }
    @Override
    public List<Task> filterTasksByQueryParameters(QueryParameterDTO queryParams,String header) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM Task t WHERE 1 = 1");

        Optional<User> optionalUser=utilityService.getUser(header);
        User user=null;
        if(optionalUser.isPresent())
        {
            user= optionalUser.get();
        }

        if (!(queryParams.getEmployeeName().equals("N/A")) && queryParams.getByUserRole().equals(User.UserRole.Supervisor)) {
            jpql.append(" AND t.assignee = :assignee");
        }

        if (queryParams.getTaskStatus()!=null && queryParams.getByUserRole().equals(User.UserRole.Supervisor)) {
            jpql.append(" AND t.taskStatus=:task_status");
        }


        if (queryParams.isByEmployeeRole()) {
            jpql.append(" AND t.assignee is NOT NULL");
        }

        if (queryParams.isByManagerRole() || queryParams.isByStatus()) {
            jpql.append(" AND t.taskStatus IS NOT NULL");
        }

        if (queryParams.isByEmployeeRole()) {
            jpql.append(" AND t.assignee is NOT NULL");
        }
        if(!(queryParams.getEmployeeName().equals("N/A"))  && queryParams.getByUserRole().equals(User.UserRole.Manager))
        {
            jpql.append(" AND t.assignee = :assignee AND t.createdBy = :manager");

        }

        if(queryParams.getTaskStatus()!=null  && queryParams.getByUserRole().equals(User.UserRole.Manager))
        {
            jpql.append(" AND t.taskStatus = :task_status AND t.createdBy = :manager");

        }

        if(queryParams.getTaskStatus()!=null  && queryParams.getByUserRole().equals(User.UserRole.Manager) && !(queryParams.getEmployeeName().equals("N/A")))
        {
            jpql.append(" AND t.taskStatus = :task_status AND t.createdBy = :manager AND t.assignee = :assignee");

        }


        if (queryParams.isByAssigned() &&  queryParams.getByUserRole().equals(User.UserRole.Employee)) {
            jpql.append(" AND t.assignee =:assignee");
        }

        TypedQuery<Task> query = entityManager.createQuery(jpql.toString(), Task.class);

        if (!(queryParams.getEmployeeName().equals("N/A"))) {
            query.setParameter("assignee", utilityService.getAssigneeByName(queryParams.getEmployeeName()));
        }
        if (queryParams.getTaskStatus()!=null && queryParams.getByUserRole().equals(User.UserRole.Supervisor)) {
            query.setParameter("task_status", queryParams.getTaskStatus());
        }

        if (queryParams.getTaskStatus()!=null  && queryParams.getByUserRole().equals(User.UserRole.Manager)) {
            query.setParameter("task_status", queryParams.getTaskStatus());
            query.setParameter("manager", user);
        }

        if (!(queryParams.getEmployeeName().equals("N/A"))  && queryParams.getByUserRole().equals(User.UserRole.Manager)) {
            query.setParameter("assignee",  utilityService.getAssigneeByName(queryParams.getEmployeeName()));
            query.setParameter("manager", user);
        }

        if (!(queryParams.getEmployeeName().equals("N/A"))  && queryParams.getByUserRole().equals(User.UserRole.Manager) && queryParams.getTaskStatus()!=null) {
            query.setParameter("assignee",  utilityService.getAssigneeByName(queryParams.getEmployeeName()));
            query.setParameter("manager", user);
            query.setParameter("task_status", queryParams.getTaskStatus());
        }

        if (queryParams.isByAssigned() &&  queryParams.getByUserRole().equals(User.UserRole.Employee)) {
            query.setParameter("assignee", user);
        }

        return query.getResultList();
    }
}
