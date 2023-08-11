package server.domain;

import jakarta.persistence.Entity;

@Entity
public class Supervisor extends User {

    public Supervisor(String firstName,String lastName,String userName,String password,UserRole userRole) {

        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setUsername(userName);
        this.setPassword(password);
        this.setUserRole(userRole);


    }

    public Supervisor() {

    }
}
