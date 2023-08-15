package server.utilities;

import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.dao.UserDao;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.User;


import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    public Optional<User.UserRole> getUserRole(String authorizationHeader) {
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



            Optional<User> authenticatedUser = userDao.getUserByUsernameAndPassword(username, password);

            if (authenticatedUser.isPresent()) {
                return Optional.of(authenticatedUser.get().getUserRole());
            }

        }

        return Optional.empty();
    }


    public Optional<Map<String, String>> getUsernamePassword(String authorizationHeader) {

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
            return Optional.of(credential);

        }

        return Optional.empty();
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
        User.UserRole employeeRole = User.UserRole.Employee;

        return authenticatedUserRole.isPresent() && authenticatedUserRole.get() == employeeRole;
    }

//    public  Optional<User> getUserByNameAndPassword(String providedUsername, String providedPassword) {
//        return null;
//    }
//        Optional<User> userOptional = userDao.getByUsername(providedUsername);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get(); // Get the User object from the Optional
//            if (user.getPassword().equals(providedPassword)) {
//                return Optional.of(user); // Return the current user's role
//            }
//        }
//
//        return Optional.empty(); // Return an empty Optional if user is not found or password doesn't match
//    }


}
