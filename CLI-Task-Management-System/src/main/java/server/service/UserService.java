package server.service;

import server.domain.User;

import java.util.List;

public interface UserService {

    String createUser(String type, String firstname, String lastname, String username, String password);

    List<User> allUsers();

    String verifyUser(String username,String pass);

}
