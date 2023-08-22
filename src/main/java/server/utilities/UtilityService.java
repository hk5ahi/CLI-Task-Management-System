package server.utilities;
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

    //throw exception if user is not present
    public Optional<User> getUser(String authorizationHeader) {
        Map<String, String> credentials = extractCredentials(authorizationHeader);
        return Optional.of(credentials)
                .flatMap(cred -> userDao.getUserByUsernameAndPassword(cred.get("username"), cred.get("password")));
    }


    public Optional<User.UserRole> getUserRole(String authorizationHeader) {
        Optional<User> authenticatedUser = getUser(authorizationHeader);
        return authenticatedUser.map(User::getUserRole);
    }

    public Optional<Map<String, String>> getUsernamePassword(String authorizationHeader) {
        return Optional.of(extractCredentials(authorizationHeader));
    }

    public Optional<Employee> getActiveEmployee(String authorizationHeader) {
        Optional<Map<String, String>> optionalMap = getUsernamePassword(authorizationHeader);

        if (optionalMap.isPresent()) {
            Map<String, String> usernamePassword = optionalMap.get();
            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");
            return employeeDao.findEmployeeByUsernameAndPassword(username, password);
        }

        return Optional.empty();
    }

    //return the Manager
    //throw exception inside that manager
    public Optional<Manager> getActiveManager(String authorizationHeader) {

        Optional<Map<String, String>> optionalMap = getUsernamePassword(authorizationHeader);

        if (optionalMap.isPresent()) {
            Map<String, String> usernamePassword = optionalMap.get();
            String username = usernamePassword.get("username");
            String password = usernamePassword.get("password");
            return managerDao.findManagerByUsernameAndPassword(username, password);
        }

        return Optional.empty();


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
        Optional<User.UserRole> authenticatedUserRole = getUserRole(header);

        return authenticatedUserRole.stream().anyMatch(role -> role == User.UserRole.Employee);
    }

}
