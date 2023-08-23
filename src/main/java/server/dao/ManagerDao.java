package server.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import server.domain.Manager;
import java.util.Optional;

public interface ManagerDao extends JpaRepository<Manager,String> {
    Optional<Manager> findManagerByUsernameAndPassword(String Username,String Password);



}
