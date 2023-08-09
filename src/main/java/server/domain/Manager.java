package server.domain;

public class Manager extends User {

    public Manager(String firstName, String lastName, String username, String password,UserRole userRole) {
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setUserRole(userRole);
    }

    public Manager() {

    }
}
