package server.dao;

import server.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> getAllUsers();

    void addUser(User user);

    void initializeUsers();

    boolean userExist(String userName);

    Optional<User> getByUsername(String username);

}


