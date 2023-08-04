package server.dao;

import server.domain.User;

import java.util.List;

public interface UserDao {

    List<User> getallUsers();

    void addUser(User user);

}


