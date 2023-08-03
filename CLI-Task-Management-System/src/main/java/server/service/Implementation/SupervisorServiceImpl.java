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

    private SupervisorDao supervisorDao;

    private  final UserDao userDao;
    private Supervisor supervisor;
    @Autowired
    public SupervisorServiceImpl(UserDao userDao,SupervisorDao supervisorDao) {
        this.supervisorDao=supervisorDao;
        this.userDao = userDao;
        supervisor = supervisorDao.getSupervisorInfo();
    }



    @Override
    public void initializeSupervisor() {


        supervisorDao.setSupervisorInfo("Muhammad", "Asif", "m.asif", "Ts12", "Supervisor");
        userDao.addUser(supervisor);

    }


    @Override
    public Supervisor verifyCredentials(String providedUsername, String providedPassword) {
        initializeSupervisor();

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

        System.out.println("The Supervisor of the System is: ");
        System.out.println(supervisor.getFirstName() + " " + supervisor.getLastName());
    }



}