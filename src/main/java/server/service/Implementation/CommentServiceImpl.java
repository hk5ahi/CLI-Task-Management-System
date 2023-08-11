package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.CommentDao;
import server.dao.SupervisorDao;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;
    private final UtilityService utilityService;
    private final TaskDao taskDao;
    private final UserDao userDao;


    public CommentServiceImpl(CommentDao commentDao, UtilityService utilityService, TaskDao taskDao, SupervisorDao supervisorDao, UserDao userDao) {
        this.commentDao = commentDao;
        this.utilityService = utilityService;
        this.taskDao = taskDao;

        this.userDao = userDao;
    }

    @Override
    public void addCommentByUser(CommentDTO comment, String header) {
        User user = getUserFromHeader(header);

        if (user instanceof Supervisor supervisor) {
            addComment(comment.getMessage(), supervisor, comment.getTitle());
        } else if (user instanceof Manager manager) {
            addComment(comment.getMessage(), manager, comment.getTitle());
        } else if (user instanceof Employee employee) {
            addComment(comment.getMessage(), employee, comment.getTitle());
        } else {
            throw new ForbiddenAccessException();
        }
    }

    private User getUserFromHeader(String header) {
        Optional<User> optionalSupervisor =userDao.getUserByUserRole(User.UserRole.Supervisor);
        if (utilityService.isAuthenticatedSupervisor(header) && optionalSupervisor.isPresent()) {
            return optionalSupervisor.get();
        }

        Optional<Manager> optionalManager = utilityService.getActiveManager(header);
        if (utilityService.isAuthenticatedManager(header) && optionalManager.isPresent()) {
            return optionalManager.get();
        }

        Optional<Employee> optionalEmployee = utilityService.getActiveEmployee(header);
        if (utilityService.isAuthenticatedEmployee(header) && optionalEmployee.isPresent()) {
            return optionalEmployee.get();
        }

        throw new ForbiddenAccessException();
    }



    @Override
    public Optional<List<CommentDTO>> getCommentByController(String title, String header) {
        if (utilityService.isAuthenticatedSupervisor(header)) {
            if (title.isEmpty()) {
                throw new NotFoundException();
            } else {
                return getComment(title);
            }
        } else {
            throw new ForbiddenAccessException();
        }
    }

    @Override
    public void addComment(String message, User person, String taskTitle) {
        Comment comment = new Comment();

        Task task = taskDao.findByTitle(taskTitle)
                .orElseThrow(BadRequestException::new);

        String userName = person.getUsername();
        String assignee = task.getAssignee() != null ? task.getAssignee().getUsername() : "N/A";
        User.UserRole personRole = person.getUserRole();

        if (!personRole.equals(User.UserRole.Supervisor)) {
            if (personRole.equals(User.UserRole.Manager) && !task.getCreatedBy().getUsername().equals(userName)) {
                throw new BadRequestException();
            } else if (personRole.equals(User.UserRole.Employee) && task.getAssignee() != null && !assignee.equals(userName)) {
                throw new BadRequestException();
            }
        }

        comment.setBody(message);
        comment.setCreatedAt(Instant.now());
        comment.setCreatedBy(person);
        comment.addTaskForComment(task);
        commentDao.saveAndFlush(comment);
    }

    @Override
    public Optional<List<CommentDTO>> getComment(String title) {
        Optional<Task> optionalTask = taskDao.findByTitle(title);

        if (optionalTask.isEmpty()) {
            return Optional.empty();
        }

        Task task = optionalTask.get();
        List<Comment> comments = commentDao.getCommentByTask(task);
        List<CommentDTO> viewCommentDTOList = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDTO commentDTO = createCommentDTOFromComment(comment);
            viewCommentDTOList.add(commentDTO);
        }

        return viewCommentDTOList.isEmpty() ? Optional.empty() : Optional.of(viewCommentDTOList);
    }

    private CommentDTO createCommentDTOFromComment(Comment comment) {

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setCreatedBy(comment.getCreatedBy().getFirstName() + " " + comment.getCreatedBy().getLastName());
        commentDTO.setMessage(comment.getBody());

        return commentDTO;
    }


}
