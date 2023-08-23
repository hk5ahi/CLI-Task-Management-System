package server.service;
import server.dto.*;
import java.util.List;
public interface TaskService {
    void createTask(TaskDTO task,String header);
    List<TaskDTO> getTasks( QueryParameterDTO queryParameterDTO,String header);
    void updateTask(String authorizationHeader,TaskDTO taskDTO);

}
