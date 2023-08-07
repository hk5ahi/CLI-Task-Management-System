package server.service;

import server.domain.User;
import server.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    String createUser(String type, String firstname, String lastname, String username, String password);

    List<User> allUsers();

    Optional<String> verifyUser(String username, String pass);

    List<UserDTO> viewAllUsers();

    void initializeUsers();

}
