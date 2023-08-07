package server.dao.implementation;

import org.springframework.stereotype.Repository;
import server.dao.SupervisorDao;
import server.domain.Supervisor;

import java.util.ArrayList;
import java.util.List;
@Repository
public class SupervisorDaoImpl implements SupervisorDao {



    private final List<Supervisor> supervisors=new ArrayList<>(List.of(new Supervisor("Muhammad", "Asif", "m.asif", "Ts12", "Supervisor")));


    @Override
    public Supervisor getSupervisorByName(String name)
    {
        for(Supervisor supervisor:supervisors)
        {
            String fullName=supervisor.getFirstName()+" "+supervisor.getLastName();
            if(fullName.equals(name))
            {
                return supervisor;
            }

        }

        return null;

    }
    public List<Supervisor> getSupervisors() {
        return supervisors;
    }

}
