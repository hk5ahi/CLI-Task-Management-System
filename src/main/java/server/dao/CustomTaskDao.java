package server.dao;
import server.domain.Task;
import server.dto.QueryParameterDTO;
import java.util.List;

public interface CustomTaskDao {
    List<Task> filterTasksByQueryParameters(QueryParameterDTO queryParams, String header);
}
