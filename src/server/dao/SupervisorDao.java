package server.dao;

import server.domain.Supervisor;

import java.util.List;

public interface SupervisorDao {
    List<Supervisor> getAllSupervisors();

    Supervisor getSupervisorByUsername(String username);

    Supervisor createSupervisor(Supervisor supervisor);

    boolean updateSupervisor(Supervisor supervisor);

    boolean deleteSupervisor(String username);
}
