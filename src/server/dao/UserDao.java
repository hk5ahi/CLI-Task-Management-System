package server.dao;

import server.domain.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    User getUserByUsername(String username);

    User createUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(String username);
}
