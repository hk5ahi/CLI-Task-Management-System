package server.domain;

import jakarta.persistence.Entity;

@Entity
public class Manager extends User {


    public Manager(String firstName, String lastName, String username, String password, UserRole userRole) {
        super(firstName, lastName, username, password, userRole);
    }

    public Manager() {

    }
}
