package server.domain;

import jakarta.persistence.Entity;

@Entity
public class Employee extends User {


    public Employee(String firstName, String lastName, String username, String password, UserRole userRole) {
        super(firstName, lastName, username, password, userRole);
    }

    public Employee() {

    }
}
