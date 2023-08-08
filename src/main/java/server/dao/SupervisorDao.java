package server.dao;

import server.domain.Supervisor;

import java.util.List;
import java.util.Optional;

public interface SupervisorDao {

    Optional<Supervisor> getSupervisorByName(String name);

    List<Supervisor> getSupervisors();

    Optional<Supervisor> getByUserName(String username);
}
