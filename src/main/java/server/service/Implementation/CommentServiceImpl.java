package server.service.Implementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.CommentDao;
import server.dao.TaskDao;
import server.dao.UserDao;
import server.domain.*;
import server.dto.CommentDTO;
import server.exception.BadRequestException;
import server.exception.ForbiddenAccessException;
import server.exception.NotFoundException;
import server.service.CommentService;
import server.utilities.UtilityService;
import java.time.Instant;
import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;
    private final UtilityService utilityService;
    private final TaskDao taskDao;
    private final UserDao userDao;

    private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    public CommentServiceImpl(CommentDao commentDao, UtilityService utilityService, TaskDao taskDao, UserDao userDao) {
        this.commentDao = commentDao;
        this.utilityService = utilityService;
        this.taskDao = taskDao;

        this.userDao = userDao;
    }

    @Transactional
    @Override
    public void addCommentByUser(CommentDTO comment, String header) {
        Optional<User> optionalUser = getUserFromHeader(header);

        User user = optionalUser.orElseThrow(() -> {
            log.error("The requesting user is not valid");
            return new ForbiddenAccessException("Only Supervisor, Manager, and Employee can add comments to a Task");
        });

        if (user instanceof Supervisor supervisor) {
            addComment(comment.getMessage(), supervisor, comment.getTitle());
        } else if (user instanceof Manager manager) {
            addComment(comment.getMessage(), manager, comment.getTitle());
        } else if (user instanceof Employee employee) {
            addComment(comment.getMessage(), employee, comment.getTitle());
        }
    }

    private Optional<User> getUserFromHeader(String header) {
        Optional<User> optionalSupervisor =userDao.getUserByUserRole(User.UserRole.Supervisor);
        if (utilityService.isAuthenticatedSupervisor(header) && optionalSupervisor.isPresent()) {
            return optionalSupervisor;
        }

        if (utilityService.isAuthenticatedManager(header) ) {
            Manager manager = utilityService.getActiveManager(header);
            return Optional.of(manager);
        }


        if (utilityService.isAuthenticatedEmployee(header) )
        {
            Employee employee = utilityService.getActiveEmployee(header);
            return Optional.of(employee);
        }

        return Optional.empty();
    }

    @Override
    public List<CommentDTO> getComment(String title, String header) {
        if (utilityService.isAuthenticatedSupervisor(header)) {
            if (title.isEmpty()) {
                log.error("The Task does not exist");
                throw new NotFoundException("There is no Task present to get its comments.");
            } else {
                return retrieveComment(title);
            }
        } else {
            log.error("The requesting user is not Supervisor");
            throw  new ForbiddenAccessException("Only Supervisor can view the comments");
        }
    }


    private void addComment(String message, User person, String taskTitle) {
        Comment comment = new Comment();

        Task task = taskDao.findByTitle(taskTitle)
                .orElseThrow(BadRequestException::new);

        String userName = person.getUsername();
        String assignee = task.getAssignee() != null ? task.getAssignee().getUsername() : "N/A";
        User.UserRole personRole = person.getUserRole();

        if (!personRole.equals(User.UserRole.Supervisor)) {
            if (personRole.equals(User.UserRole.Manager) && !task.getCreatedBy().getUsername().equals(userName)) {
                log.error("The manager {} have not permission to add comments for this task",person.getUsername());
                throw  new BadRequestException("The User is not valid");
            } else if (personRole.equals(User.UserRole.Employee) && task.getAssignee() != null && !assignee.equals(userName)) {
                log.error("The employee {} have not permission to add comments for this task or the task is not assigned yet",person.getUsername());
                throw  new BadRequestException("The User is not valid");
            }
        }
        comment.setBody(message);
        comment.setCreatedAt(Instant.now());
        comment.setCreatedBy(person);
        comment.addTaskForComment(task);
        commentDao.save(comment);
    }


    private List<CommentDTO> retrieveComment(String title) {
        Optional<Task> optionalTask = taskDao.findByTitle(title);

        if (optionalTask.isEmpty()) {
            return Collections.emptyList();
        }

        Task task = optionalTask.get();
        List<Comment> comments = commentDao.getCommentByTask(task);
        List<CommentDTO> viewCommentDTOList = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDTO commentDTO = createCommentDTOFromComment(comment);
            viewCommentDTOList.add(commentDTO);
        }

        return viewCommentDTOList;
    }

    private CommentDTO createCommentDTOFromComment(Comment comment) {

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setCreatedBy(comment.getCreatedBy().getFirstName() + " " + comment.getCreatedBy().getLastName());
        commentDTO.setMessage(comment.getBody());

        return commentDTO;
    }

}
