package server.dao.implementation;

import org.springframework.stereotype.Repository;
import server.dao.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

    private String Name = "Muhammad Hanan";

    @Override
    public String getName() {
        return Name;

    }
}
