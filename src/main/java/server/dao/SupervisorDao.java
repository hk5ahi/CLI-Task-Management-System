package server.dao;

import server.domain.Supervisor;

import java.util.List;

public interface SupervisorDao {

    Supervisor getSupervisorByName(String name);

    List<Supervisor> getSupervisors();
}
