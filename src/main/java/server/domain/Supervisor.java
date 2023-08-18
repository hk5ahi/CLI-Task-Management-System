package server.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.Entity;
@JsonTypeName("Supervisor")
@Entity
public class Supervisor extends User {

    public Supervisor(String firstName, String lastName, String username, String password, UserRole userRole) {
        super(firstName, lastName, username, password, userRole);
    }

    public Supervisor() {

    }
}