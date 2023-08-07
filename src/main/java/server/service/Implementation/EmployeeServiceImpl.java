package server.service.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.TaskDao;
import server.dao.UserDao;
import server.domain.Employee;
import server.domain.Task;
import server.domain.User;
import server.exception.ForbiddenAccessException;
import server.service.EmployeeService;

import java.util.List;
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDao employeeDao;
    private final TaskDao taskDao;


    public EmployeeServiceImpl(EmployeeDao employeeDao, TaskDao taskDao) {
        this.employeeDao = employeeDao;
        this.taskDao = taskDao;
    }


    @Override
    public ResponseEntity<String> addTotalTime(double time, String title, Employee employee) {

        Task providedtask=null;
        if (title != null) {
            for (Task task : taskDao.getAll()) {
                if (task != null && task.getTitle() != null && task.getTitle().equalsIgnoreCase(title)) {
                    providedtask=task;
                }
            }
        }

        if (providedtask == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
        String assigneeName=providedtask.getAssignee().getFirstName()+" "+providedtask.getAssignee().getLastName();
        String employeeName=employee.getFirstName()+" "+employee.getLastName();
        if(assigneeName.equals(employeeName)) {
            providedtask.setTotal_time(time);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else {

            throw new ForbiddenAccessException();
        }
    }

    @Override
    public List<Employee> getAllEmployees() {

        return employeeDao.getEmployees();
    }


}
