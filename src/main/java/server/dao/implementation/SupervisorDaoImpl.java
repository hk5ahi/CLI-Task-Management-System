package server.dao.implementation;

import org.springframework.stereotype.Repository;
import server.dao.SupervisorDao;
import server.domain.Supervisor;
import server.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SupervisorDaoImpl implements SupervisorDao {

    private final List<Supervisor> supervisors=new ArrayList<>(List.of(new Supervisor("Muhammad", "Asif", "m.asif", "Ts12", User.UserRole.Supervisor)));


    @Override
    public Optional<Supervisor> getSupervisorByName(String name) {
        return supervisors.stream()
                .filter(supervisor -> {
                    String fullName = supervisor.getFirstName() + " " + supervisor.getLastName();
                    return fullName.equals(name);
                })
                .findFirst();
    }

    public List<Supervisor> getSupervisors() {
        return supervisors;
    }

    @Override
    public Optional<Supervisor> getByUserName(String username) {
        return supervisors.stream()
                .filter(supervisor -> supervisor.getUsername().equals(username))
                .findFirst();
    }

}
