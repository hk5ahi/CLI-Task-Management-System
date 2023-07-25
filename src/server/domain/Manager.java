package server.domain;

public class Manager extends User {

    public Manager(String firstName, String lastName, String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public Manager() {

    }
}
