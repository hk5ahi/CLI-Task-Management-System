package server.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @Column(name = "username", unique = true, length = 10, nullable = false, columnDefinition = "TEXT")
    private String username;

    @Column(name = "first_name", length = 10, nullable = false, columnDefinition = "TEXT")
    private String firstName;

    @Column(name = "last_name", length = 20, columnDefinition = "TEXT")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    public User(String firstName, String lastName, String username, String password, UserRole userRole) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
        this.password = password;
    }

    public User() {

    }

    public enum UserRole {
        Supervisor,
        Manager,
        Employee
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
