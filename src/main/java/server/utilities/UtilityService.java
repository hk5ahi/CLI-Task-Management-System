package server.utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.dao.UserDao;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.User;


import java.util.*;

@Service
public class UtilityService {
    private final ManagerDao managerDao;
    private final EmployeeDao employeeDao;
    private final UserDao userDao;
    private final Logger log = LoggerFactory.getLogger(UtilityService.class);

    public UtilityService( ManagerDao managerDao, EmployeeDao employeeDao, UserDao userDao) {

        this.managerDao = managerDao;
        this.employeeDao = employeeDao;
        this.userDao = userDao;
    }

    public Employee getAssigneeByName(String fullName) {
        if(fullName!=null) {
            String[] nameParts = fullName.split(" ");
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";
            Optional<Employee> optionalEmployee = employeeDao.getEmployeeByFirstNameAndLastName(firstName, lastName);
            return optionalEmployee.orElseThrow(NotFoundException::new);
        }
        else {
            return null;
        }
    }
    private Map<String, String> extractCredentials(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String encodedCredentials = authorizationHeader.substring("Basic ".length());
            byte[] decodedCredentials = Base64.getDecoder().decode(encodedCredentials);
            String credentials = new String(decodedCredentials);
            String[] usernameAndPassword = credentials.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            Map<String, String> credential = new HashMap<>();
            credential.put("username", username);
            credential.put("password", password);
            return credential;
        }
        return Collections.emptyMap();
    }

    public User getUser(String authorizationHeader) throws NotFoundException {
        Map<String, String> credentials = extractCredentials(authorizationHeader);
        return Optional.of(credentials)
                .flatMap(cred -> userDao.getUserByUsernameAndPassword(cred.get("username"), cred.get("password")))
                .orElseThrow(NotFoundException::new);
    }



    public User.UserRole getUserRole(String authorizationHeader) {
        return getUser(authorizationHeader).getUserRole();
    }

    public Optional<Map<String, String>> getUsernamePassword(String authorizationHeader) {
        return Optional.of(extractCredentials(authorizationHeader));
    }

    public Employee getActiveEmployee(String authorizationHeader) throws NotFoundException {
        Optional<Map<String, String>> optionalMap = getUsernamePassword(authorizationHeader);

        if (optionalMap.isPresent()) {
            Map<String, String> usernamePassword = optionalMap.get();
            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");
            Optional<Employee> employeeOptional = employeeDao.findEmployeeByUsernameAndPassword(username, password);
            if (employeeOptional.isPresent()) {
                return employeeOptional.get();
            }
        }

        log.error("Employee Not Found.");
        throw new NotFoundException("Employee Not Found");
    }

    public Manager getActiveManager(String authorizationHeader) throws NotFoundException {
        Optional<Map<String, String>> optionalMap = getUsernamePassword(authorizationHeader);

        if (optionalMap.isPresent()) {
            Map<String, String> usernamePassword = optionalMap.get();
            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");
            Optional<Manager> managerOptional = managerDao.findManagerByUsernameAndPassword(username, password);
            if (managerOptional.isPresent()) {
                return managerOptional.get();
            }
        }

        log.error("Manager Not Found.");
        throw new NotFoundException("Manager Not Found");
    }


    public boolean isAuthenticatedSupervisor(String header) {
        Optional<User.UserRole> authenticatedUserRole = getUserRole(header);
        User.UserRole supervisorRole = User.UserRole.Supervisor;

        return authenticatedUserRole.isPresent() && authenticatedUserRole.get() == supervisorRole;
    }
    public boolean isAuthenticatedManager(String header) {
        Optional<User.UserRole> authenticatedUserRole = getUserRole(header);
        User.UserRole managerRole = User.UserRole.Manager;

        return authenticatedUserRole.isPresent() && authenticatedUserRole.get() == managerRole;
    }

    public boolean isAuthenticatedEmployee(String header) {
        User.UserRole authenticatedUserRole = getUserRole(header);

        return authenticatedUserRole.equals(User.UserRole.Employee);
    }

}
