package server.service.Implementation;
import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.TaskDao;
import server.domain.Employee;
import server.domain.Task;
import server.exception.BadRequestException;
import server.exception.ForbiddenAccessException;
import server.service.EmployeeService;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDao employeeDao;
    private final TaskDao taskDao;


    public EmployeeServiceImpl(EmployeeDao employeeDao, TaskDao taskDao) {
        this.employeeDao = employeeDao;
        this.taskDao = taskDao;
    }


    @Override
    public void updateTotalTime(double time, String title, Employee employee) {
        Optional<Task> optionalTask = taskDao.getTaskByTitle(title);

        if (optionalTask.isEmpty()) {
            throw new BadRequestException();
        }

        Task providedTask = optionalTask.get();
        String assigneeUserName = providedTask.getAssignee().getUsername();
        String employeeUserName = employee.getUsername();

        if (assigneeUserName.equals(employeeUserName)) {
            providedTask.setTotal_time(time);

        } else {
            throw new ForbiddenAccessException();
        }
    }


    @Override
    public List<Employee> getAllEmployees() {

        return employeeDao.getEmployees();
    }


}
