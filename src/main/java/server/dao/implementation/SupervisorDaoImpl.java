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
    public Optional<Supervisor> getSupervisorByName(String name)
    {
        for(Supervisor supervisor:supervisors)
        {
            String fullName=supervisor.getFirstName()+" "+supervisor.getLastName();
            if(fullName.equals(name))
            {
                return Optional.of(supervisor);
            }

        }

        return Optional.empty();

    }
    public List<Supervisor> getSupervisors() {
        return supervisors;
    }

    @Override
    public Optional<Supervisor> getByUserName(String username)
    {
        for(Supervisor supervisor:supervisors)
        {
            if(supervisor.getUsername().equals(username))
            {
                return Optional.of(supervisor);
            }

        }
        return Optional.empty();

    }

}
