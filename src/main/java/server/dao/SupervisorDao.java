package server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import server.domain.Supervisor;

import java.util.List;
import java.util.Optional;

public interface SupervisorDao extends JpaRepository<Supervisor,String> {

//    Optional<Supervisor> getSupervisorByName(String name);
//
//    List<Supervisor> getSupervisors();
//
//    Optional<Supervisor> getByUserName(String username);
}
