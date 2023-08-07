package server.domain;

public class Supervisor extends User {

    public Supervisor(String firstName,String lastName,String userName,String password,String userRole) {

        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setUsername(userName);
        this.setPassword(password);
        this.setUserRole(userRole);


    }
}
