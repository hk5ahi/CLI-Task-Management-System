package server.dto;
import server.domain.Task;


public class QueryParameterDTO {

    public static QueryParameterDTO createObjectFromQueryParameters(
            Task.Status byTaskStatus, String byUserName) {

        QueryParameterDTO queryParameterDTO = new QueryParameterDTO();

        queryParameterDTO.setTaskStatus(byTaskStatus);
        queryParameterDTO.setUserName(byUserName);
        return queryParameterDTO;
    }



    public Task.Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Task.Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    private Task.Status taskStatus;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;


}
