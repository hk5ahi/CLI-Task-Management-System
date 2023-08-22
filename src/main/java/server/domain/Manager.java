package server.domain;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.Entity;
@JsonTypeName("Manager")
@Entity
public class Manager extends User {


    public Manager(String firstName, String lastName, String username, String password, UserRole userRole) {
        super(firstName, lastName, username, password, userRole);
    }

    public Manager()
    {}

    }

