package server.service;
import server.domain.User;
import server.dto.UserDTO;
import java.util.List;
public interface UserService {
    void initializeUsers();
    void create(User user,String header);
    List<UserDTO> getAllUsers(String header);

}
