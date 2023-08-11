package server.domain;

import jakarta.persistence.Entity;

@Entity
public class Employee extends User {

    public Employee(String firstname, String lastname, String username, String password,User.UserRole userRole) {
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstName(firstname);
        this.setLastName(lastname);
        this.setUserRole(userRole);
    }

    public Employee() {
    }


}
