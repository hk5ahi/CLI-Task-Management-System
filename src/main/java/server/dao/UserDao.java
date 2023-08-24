package server.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import server.domain.User;
import java.util.Optional;

public interface UserDao extends JpaRepository<User,String> {
    Optional<User> getUserByUsernameAndPassword(String Username, String Password);
    boolean existsByUsername(String Username);
    Optional<User> getUserByUserRole(User.UserRole userRole);
    Optional<User> getUserByUsername(String Username);

}


