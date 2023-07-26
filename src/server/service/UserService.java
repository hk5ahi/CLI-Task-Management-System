package server.service;

public interface UserService {

    void createUser(String type, String firstname, String lastname, String username, String password);
}
