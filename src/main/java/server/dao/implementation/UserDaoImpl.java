package server.dao.implementation;

import org.springframework.stereotype.Repository;
import server.dao.UserDao;
import server.domain.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private static UserDaoImpl instance;  // Singleton instance
    private final List<User> users=new ArrayList<>();

    private UserDaoImpl() {

    }

    // Method to get the Singleton instance
    public static UserDaoImpl getInstance() {
        if (instance == null) {
            instance = new UserDaoImpl();
        }
        return instance;
    }
    public void addUser(User user)
    {
        users.add(user);

    }
    public List<User> getallUsers()
    {
        return users;

    }
}
