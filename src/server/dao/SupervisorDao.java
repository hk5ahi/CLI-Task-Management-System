package server.dao;

import server.domain.Supervisor;

public interface SupervisorDao {

    void setSupervisorInfo(String firstName, String lastName, String username, String password, String userRole);

    Supervisor getSupervisorInfo();
}
