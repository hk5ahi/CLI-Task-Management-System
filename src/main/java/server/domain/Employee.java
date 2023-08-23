package server.domain;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.Entity;
@JsonTypeName("Employee")
@Entity
public class Employee extends User {
    public Employee(String firstName, String lastName, String username, String password, UserRole userRole) {
        super(firstName, lastName, username, password, userRole);
    }
    public Employee() {

    }
}
