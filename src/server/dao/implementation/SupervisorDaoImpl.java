package server.dao;

import server.domain.Supervisor;

import java.util.ArrayList;
import java.util.List;

public class SupervisorDaoImpl implements SupervisorDao {

    private List<Supervisor> supervisors = new ArrayList<>();

    @Override
    public List<Supervisor> getAllSupervisors() {
        return supervisors;
    }

    @Override
    public Supervisor getSupervisorByUsername(String username) {
        for (Supervisor supervisor : supervisors) {
            if (supervisor.getUsername().equals(username)) {
                return supervisor;
            }
        }
        return null;
    }

    @Override
    public Supervisor createSupervisor(Supervisor supervisor) {
        supervisors.add(supervisor);
        return supervisor;
    }

    @Override
    public boolean updateSupervisor(Supervisor supervisor) {
        for (int i = 0; i < supervisors.size(); i++) {
            if (supervisors.get(i).getUsername().equals(supervisor.getUsername())) {
                supervisors.set(i, supervisor);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteSupervisor(String username) {
        for (Supervisor supervisor : supervisors) {
            if (supervisor.getUsername().equals(username)) {
                supervisors.remove(supervisor);
                return true;
            }
        }
        return false;
    }
}
