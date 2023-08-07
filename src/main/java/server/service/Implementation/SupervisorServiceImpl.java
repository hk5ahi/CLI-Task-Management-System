package server.service.Implementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.dao.SupervisorDao;
import server.dao.UserDao;
import server.dao.implementation.SupervisorDaoImpl;
import server.domain.Supervisor;
import server.service.SupervisorService;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    private final SupervisorDao supervisorDao;

    @Autowired
    public SupervisorServiceImpl(SupervisorDao supervisorDao) {
        this.supervisorDao=supervisorDao;


    }

    @Override
    public Supervisor getAndVerify(String providedUsername, String providedPassword) {

        String supervisorName=supervisorDao.getSupervisors().get(0).getFirstName()+" "+supervisorDao.getSupervisors().get(0).getLastName();

        Supervisor supervisor=supervisorDao.getSupervisorByName(supervisorName);
        String storedUsername = supervisor.getUsername();
        String storedPassword = supervisor.getPassword();

        if (storedUsername.equals(providedUsername) && storedPassword.equals(providedPassword)) {
            return supervisor; // Return the current Supervisor object
        } else {
            return null; // If the credentials don't match, return null
        }
    }


    @Override
    public void viewSupervisor() {

        Supervisor supervisor=supervisorDao.getSupervisorByName("Muhammad Asif");
        System.out.println("The Supervisor of the System is: ");
        System.out.println(supervisor.getFirstName() + " " + supervisor.getLastName());
    }



}
