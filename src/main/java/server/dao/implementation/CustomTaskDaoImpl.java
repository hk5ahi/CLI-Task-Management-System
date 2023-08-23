package server.dao.implementation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import server.dao.CustomTaskDao;

import server.dao.UserDao;
import server.domain.Task;
import server.domain.User;
import server.dto.AuthUserDTO;
import server.dto.QueryParameterDTO;

import server.utilities.UtilityService;

import java.util.List;
import java.util.Optional;
@Repository
public class CustomTaskDaoImpl implements CustomTaskDao {

    @PersistenceContext
    private final EntityManager entityManager;

    private final UtilityService utilityService;

    private final UserDao userDao;

    public CustomTaskDaoImpl(EntityManager entityManager, UtilityService utilityService, UserDao userDao) {
        this.entityManager = entityManager;
        this.utilityService = utilityService;
        this.userDao = userDao;

    }
    @Override
    public List<Task> filterTasksByQueryParameters(QueryParameterDTO queryParams,String header) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM Task t WHERE 1 = 1");
        AuthUserDTO authUserDTO = utilityService.getAuthUser(header);
        String userName= queryParams.getUserName();
        Optional<User> optionalRequestedUser = userDao.getUserByUsername(userName);
        User requestedUser = null;
        if ((optionalRequestedUser.isPresent())) {
                requestedUser = optionalRequestedUser.get();
        }

        User loggedInUser= utilityService.getUser(header);

        if (loggedInUser.getUserRole().equals(User.UserRole.Supervisor) && (requestedUser!=null && requestedUser.getUserRole().equals(User.UserRole.Employee)) ) {
            jpql.append(" AND t.assignee = :assignee");
        }

        if (loggedInUser.getUserRole().equals(User.UserRole.Supervisor) && (requestedUser!=null && requestedUser.getUserRole().equals(User.UserRole.Manager)) ) {
            jpql.append(" AND t.createdBy = :manager");
        }


        if(loggedInUser.getUserRole().equals(User.UserRole.Supervisor) && queryParams.getUserName()!=null && queryParams.getTaskStatus()==null)
        {
              jpql.append(" AND t.taskStatus IS NOT NULL");

        }

        if (queryParams.getTaskStatus()!=null && loggedInUser.getUserRole().equals(User.UserRole.Supervisor)) {
            jpql.append(" AND t.taskStatus=:task_status");
        }

        if(loggedInUser.getUserRole().equals(User.UserRole.Manager) && queryParams.getTaskStatus()!=null)
        {
            jpql.append(" AND t.taskStatus = :task_status AND t.createdBy = :manager");

        }

        if(loggedInUser.getUserRole().equals(User.UserRole.Manager) && (requestedUser!=null && requestedUser.getUserRole().equals(User.UserRole.Employee)))
        {
            jpql.append(" AND t.assignee = :assignee AND t.createdBy = :manager");

        }
        if(loggedInUser.getUserRole().equals(User.UserRole.Manager) && (requestedUser!=null && requestedUser.getUserRole().equals(User.UserRole.Employee)) && queryParams.getTaskStatus()!=null)
        {
            jpql.append(" AND t.assignee = :assignee AND t.createdBy = :manager AND t.taskStatus = :task_status");

        }

        if(loggedInUser.getUserRole().equals(User.UserRole.Employee) && queryParams.getTaskStatus()!=null)
        {
            jpql.append(" AND t.assignee = :assignee  AND t.taskStatus = :task_status");

        }

        if(loggedInUser.getUserRole().equals(User.UserRole.Employee) && queryParams.getTaskStatus()!=null && requestedUser==null)
        {
            jpql.append(" AND t.assignee = :assignee");

        }
        if(loggedInUser.getUserRole().equals(User.UserRole.Employee) && queryParams.getTaskStatus()==null)
        {
            jpql.append(" AND t.assignee = :assignee ");
        }


        if(loggedInUser.getUserRole().equals(User.UserRole.Manager) && queryParams.getTaskStatus()==null &&  requestedUser==null)
        {
            jpql.append(" AND t.createdBy = :manager");

        }
        if(loggedInUser.getUserRole().equals(User.UserRole.Manager) && queryParams.getTaskStatus()==null && (requestedUser!=null && requestedUser.getUserRole().equals(User.UserRole.Employee)))
        {
            jpql.append(" AND t.assignee = :assignee AND t.createdBy = :manager");
        }

        TypedQuery<Task> query = entityManager.createQuery(jpql.toString(), Task.class);

        if (loggedInUser.getUserRole().equals(User.UserRole.Supervisor) && requestedUser!=null && requestedUser.getUserRole().equals(User.UserRole.Employee)) {
            query.setParameter("assignee", requestedUser);
        }

        if (authUserDTO!=null && authUserDTO.getUserRole().equals(User.UserRole.Supervisor) && (requestedUser!=null && requestedUser.getUserRole().equals(User.UserRole.Manager)) ) {
            query.setParameter("manager", requestedUser);
        }
        if (queryParams.getTaskStatus()!=null && loggedInUser.getUserRole().equals(User.UserRole.Supervisor)) {
            query.setParameter("task_status", queryParams.getTaskStatus());
        }

        if(loggedInUser.getUserRole().equals(User.UserRole.Employee) && queryParams.getTaskStatus()!=null)
        {
            query.setParameter("task_status", queryParams.getTaskStatus());
            query.setParameter("assignee", requestedUser);

        }
        if(loggedInUser.getUserRole().equals(User.UserRole.Manager) && queryParams.getTaskStatus()!=null)
        {
            query.setParameter("task_status", queryParams.getTaskStatus());
            query.setParameter("manager", loggedInUser);
        }

        if(loggedInUser.getUserRole().equals(User.UserRole.Manager) && (requestedUser!=null && requestedUser.getUserRole().equals(User.UserRole.Employee)))
        {
            query.setParameter("assignee", requestedUser);
            query.setParameter("manager", loggedInUser);
        }

        if(loggedInUser.getUserRole().equals(User.UserRole.Manager) && (requestedUser!=null && requestedUser.getUserRole().equals(User.UserRole.Employee)) && queryParams.getTaskStatus()!=null)
        {
            query.setParameter("assignee", requestedUser);
            query.setParameter("manager", loggedInUser);
            query.setParameter("task_status", queryParams.getTaskStatus());
        }
        if(loggedInUser.getUserRole().equals(User.UserRole.Employee) && queryParams.getTaskStatus()!=null && queryParams.getUserName()==null && loggedInUser!=null)
        {
            query.setParameter("assignee", loggedInUser);
            query.setParameter("task_status", queryParams.getTaskStatus());
        }

        if(loggedInUser.getUserRole().equals(User.UserRole.Employee) && queryParams.getTaskStatus()!= Task.Status.CREATED && queryParams.getTaskStatus()!= Task.Status.IN_PROGRESS && queryParams.getTaskStatus()!= Task.Status.IN_REVIEW&& queryParams.getTaskStatus()!= Task.Status.COMPLETED)
        {
            query.setParameter("assignee", loggedInUser);
        }

        if(loggedInUser.getUserRole().equals(User.UserRole.Employee) && queryParams.getTaskStatus()!=null && requestedUser==null)
        {
            query.setParameter("assignee", loggedInUser);

        }

        if(loggedInUser.getUserRole().equals(User.UserRole.Manager) && queryParams.getTaskStatus()!= Task.Status.CREATED && queryParams.getTaskStatus()!= Task.Status.IN_PROGRESS && queryParams.getTaskStatus()!= Task.Status.IN_REVIEW && queryParams.getTaskStatus()!= Task.Status.COMPLETED && requestedUser==null)
        {
            query.setParameter("manager", loggedInUser);
        }
        if(loggedInUser.getUserRole().equals(User.UserRole.Manager) && queryParams.getTaskStatus()!= Task.Status.CREATED && queryParams.getTaskStatus()!= Task.Status.IN_PROGRESS && queryParams.getTaskStatus()!= Task.Status.IN_REVIEW && queryParams.getTaskStatus()!= Task.Status.COMPLETED && requestedUser!=null && requestedUser.getUserRole().equals(User.UserRole.Employee))
        {
            query.setParameter("assignee", requestedUser);
            query.setParameter("manager", loggedInUser);
        }

        return query.getResultList();
    }
}
