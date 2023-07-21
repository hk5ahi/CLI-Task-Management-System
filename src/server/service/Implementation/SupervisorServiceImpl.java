package server.service.Implementation;
import server.domain.Supervisor;
import server.service.SupervisorService;
import static server.domain.Supervisor.getInstance;

public class SupervisorServiceImpl implements SupervisorService {


    @Override
    public Supervisor verifyCredentials(String providedUsername, String providedPassword) {
        if (getInstance().getUsername().equals(providedUsername) && getInstance().getPassword().equals(providedPassword)) {
            return getInstance(); // Return the current Supervisor object
        } else {
            return null; // If the credentials don't match, return null
        }
    }


    @Override
    public void viewSupervisor() {
        System.out.println("The Supervisor of the System is: ");
        System.out.println(getInstance().getFirstName() + " " + getInstance().getLastName());

    }





}
