package server.service.Implementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.UserDao;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Supervisor;
import server.domain.User;
import server.dto.UserDTO;
import server.exception.BadRequestException;
import server.exception.ForbiddenAccessException;
import server.service.UserService;
import server.utilities.UtilityService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UtilityService utilityService;
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    public UserServiceImpl(UserDao userDao, UtilityService utilityService) {

        this.userDao = userDao;
        this.utilityService = utilityService;
    }
    @Override
    @Transactional
    public void initializeUsers()
    {
        List<User> usersList = Arrays.asList(
                new Supervisor("Muhammad", "Asif", "m.asif", "Ts12", User.UserRole.Supervisor),
                new Employee("Muhammad", "Hanan", "m.hanan", "Ts12", User.UserRole.Employee),
                new Manager("Muhammad", "Ubaid", "m.ubaid", "Ts12", User.UserRole.Manager)
        );
        for (User user : usersList) {
            if (!userDao.existsByUsername(user.getUsername())) {

                userDao.save(user);
            } else {
                log.error("The user {} already exists.",user.getUsername());
                throw new BadRequestException("The user is not able to initialize");
            }
        }
    }
    @Override
    public List<UserDTO> getAllUsers(String header)
    {

        if (utilityService.isAuthenticatedSupervisor(header)) {
            return allUsers();
        } else {
            log.error("The requesting user is not Supervisor.");
            throw new ForbiddenAccessException("Only Supervisor can view all users");
        }
    }
    @Override
    @Transactional
    public void create(User user, String header) {
        if (!utilityService.isAuthenticatedSupervisor(header)) {
            log.error("The requesting user is not Supervisor.");
            throw new ForbiddenAccessException("Only Supervisor can create a User");
        }
        storeUser(user);
    }

    private List<UserDTO> allUsers()
    {
        List<UserDTO> userDTOS=new ArrayList<>();

        for(User user:userDao.findAll())
        {
            UserDTO userDTO=new UserDTO();
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setUserRole(user.getUserRole());
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    private void storeUser(User user) {

        if(userDao.existsByUsername(user.getUsername()))
        {
            log.error("The user {} already exists.",user.getUsername());
            throw new BadRequestException("The user is not able to create");
        }
        else if (user.getUserRole().equals(User.UserRole.Employee)) {

            Employee employee=new Employee(user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getUserRole());
            userDao.save(employee);

        } else if (user.getUserRole().equals(User.UserRole.Manager)) {

           Manager manager= new Manager(user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getUserRole());
           userDao.save(manager);
        }
        else {
            log.error("The User of Type Employee and Manager can only be Created");
            throw new BadRequestException("The user is not able to create");
        }

    }
}