package server.service;

import server.domain.User;
import server.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

   void createUser(User.UserRole type, String firstname, String lastname, String username, String password);

    List<User> allUsers();

    Optional<User> getUserByNameAndPassword(String username, String pass);

    List<UserDTO> viewAllUsers();

    void initializeUsers();

    void postCreateUser(User user,String header);

    List<UserDTO> getAllUserForController(String header);

}
