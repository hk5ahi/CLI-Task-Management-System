package server.service.Implementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.dao.SupervisorDao;
import server.dao.UserDao;
import server.dao.implementation.SupervisorDaoImpl;
import server.domain.Supervisor;
import server.service.SupervisorService;

import java.util.Optional;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    private final SupervisorDao supervisorDao;

    @Autowired
    public SupervisorServiceImpl(SupervisorDao supervisorDao) {
        this.supervisorDao=supervisorDao;

    }

    @Override
    public Supervisor getAndVerify(String providedUsername, String providedPassword) {
        Optional<Supervisor> optionalSupervisor = supervisorDao.getByUserName(providedUsername);

        if (optionalSupervisor.isPresent() && optionalSupervisor.get().getPassword().equals(providedPassword)) {
            return optionalSupervisor.get();
        }

        return null;
    }



}
