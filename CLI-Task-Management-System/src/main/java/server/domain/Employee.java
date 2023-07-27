package server.domain;

public class Employee extends User {

    public Employee(String firstname, String lastname, String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstName(firstname);
        this.setLastName(lastname);
    }

    public Employee() {
    }
}
