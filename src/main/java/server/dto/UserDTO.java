package server.dto;
import server.domain.User;

public class UserDTO {

    private String firstName;
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User.UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(User.UserRole userRole) {
        this.userRole = userRole;
    }
    private String lastName;
    private User.UserRole userRole;
}
