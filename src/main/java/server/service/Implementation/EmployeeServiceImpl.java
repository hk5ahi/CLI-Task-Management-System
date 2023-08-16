package server.service.Implementation;
import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.TaskDao;
import server.domain.Employee;
import server.domain.Task;
import server.dto.TaskDTO;
import server.exception.BadRequestException;
import server.exception.ForbiddenAccessException;
import server.service.EmployeeService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final TaskDao taskDao;


    public EmployeeServiceImpl( TaskDao taskDao) {

        this.taskDao = taskDao;
    }


    @Override
    public void updateTotalTime(TaskDTO taskDTO, Employee employee) {
        Task providedTask = taskDao.findByTitle(taskDTO.getTitle())
                .orElseThrow(BadRequestException::new);

        if (!taskTimeDTOMatchesTask(taskDTO, providedTask)) {
            throw new BadRequestException();
        }

        String assigneeUserName = providedTask.getAssignee().getUsername();
        String employeeUserName = employee.getUsername();

        if (!assigneeUserName.equals(employeeUserName)) {
            throw new ForbiddenAccessException();
        }

        providedTask.setTotal_time(taskDTO.getTotal_time());
        taskDao.save(providedTask);
    }

    private boolean taskTimeDTOMatchesTask(TaskDTO taskDTO, Task task) {
        return Objects.equals(taskDTO.getDescription(), task.getDescription())
                && Objects.equals(taskDTO.getTaskStatus(), task.getTaskStatus())
                && Objects.equals(taskDTO.getCreatedBy(), task.getCreatedBy().getFirstName()+" "+task.getCreatedBy().getLastName())
                && Objects.equals(taskDTO.getAssignee(), task.getAssignee().getFirstName()+" "+task.getAssignee().getLastName());

    }



}
