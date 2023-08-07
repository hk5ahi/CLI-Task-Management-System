package server.utilities;

import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.domain.Employee;
import server.domain.Manager;
import server.service.UserService;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Service
public class UtilityService {

    private final UserService userService;
    private final ManagerDao managerDao;
    private final EmployeeDao employeeDao;

    public UtilityService(UserService userService, ManagerDao managerDao, EmployeeDao employeeDao) {
        this.userService = userService;
        this.managerDao = managerDao;
        this.employeeDao = employeeDao;
    }

    public String isAuthenticated(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            // Extract the Base64-encoded credentials from the header
            String encodedCredentials = authorizationHeader.substring("Basic ".length());

            // Decode the Base64-encoded credentials
            byte[] decodedCredentials = Base64.getDecoder().decode(encodedCredentials);
            String credentials = new String(decodedCredentials);

            // Extract the username and password from the credentials string
            String[] usernameAndPassword = credentials.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            // Now, you can perform the authentication based on the obtained username and password.
            Optional<String> authenticatedUser = userService.verifyUser(username, password);

            if (authenticatedUser.isPresent()) {
                return authenticatedUser.get();
            }
        }

        return null;
    }


    public Map<String, String> getUsernamePassword(String authorizationHeader) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            // Extract the Base64-encoded credentials from the header
            String encodedCredentials = authorizationHeader.substring("Basic ".length());

            // Decode the Base64-encoded credentials
            byte[] decodedCredentials = Base64.getDecoder().decode(encodedCredentials);
            String credentials = new String(decodedCredentials);

            // Extract the username and password from the credentials string
            String[] usernameAndPassword = credentials.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            Map<String, String> credential = new HashMap<>();
            credential.put("username", username);
            credential.put("password", password);
            return credential;

        }

        return null;
    }
    public Employee getActiveEmployee(String authorizationHeader) {
        Map<String, String> usernamePassword = getUsernamePassword(authorizationHeader);
        String username = usernamePassword.get("username");
        String password = usernamePassword.get("password");
        return employeeDao.findEmployee(username, password);
    }

    public Manager getActiveManager(String authorizationHeader) {
        Map<String, String> usernamePassword = getUsernamePassword(authorizationHeader);
        String username = usernamePassword.get("username");
        String password = usernamePassword.get("password");
        return managerDao.findManager(username, password);
    }




}
