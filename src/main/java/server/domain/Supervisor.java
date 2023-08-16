package server.domain;

import jakarta.persistence.Entity;

@Entity
public class Supervisor extends User {

    public Supervisor(String firstName, String lastName, String username, String password, UserRole userRole) {
        super(firstName, lastName, username, password, userRole);
    }

    public Supervisor() {

    }
}