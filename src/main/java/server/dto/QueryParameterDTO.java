package server.dto;
import server.domain.Task;
import server.domain.User;

public class QueryParameterDTO {

    public static QueryParameterDTO createObjectFromQueryParameters(
            boolean byStatus, boolean byEmployeeRole, boolean byAssigned,
            User.UserRole byUserRole, boolean byManagerRole,
            Task.Status byTaskStatus, String byEmployeeName) {

        QueryParameterDTO queryParameterDTO = new QueryParameterDTO();
        queryParameterDTO.setByStatus(byStatus);
        queryParameterDTO.setByEmployeeRole(byEmployeeRole);
        queryParameterDTO.setByAssigned(byAssigned);
        queryParameterDTO.setByUserRole(byUserRole);
        queryParameterDTO.setByManagerRole(byManagerRole);

        queryParameterDTO.setTaskStatus(byTaskStatus);
        queryParameterDTO.setEmployeeName(byEmployeeName);
        return queryParameterDTO;
    }
    private boolean byStatus;

    public boolean isByStatus() {
        return byStatus;
    }

    public void setByStatus(boolean byStatus) {
        this.byStatus = byStatus;
    }

    public boolean isByEmployeeRole() {
        return byEmployeeRole;
    }

    public void setByEmployeeRole(boolean byEmployeeRole) {
        this.byEmployeeRole = byEmployeeRole;
    }

    public boolean isByAssigned() {
        return byAssigned;
    }

    public void setByAssigned(boolean byAssigned) {
        this.byAssigned = byAssigned;
    }

    public User.UserRole getByUserRole() {
        return byUserRole;
    }

    public void setByUserRole(User.UserRole byUserRole) {
        this.byUserRole = byUserRole;
    }

    public boolean isByManagerRole() {
        return byManagerRole;
    }

    public void setByManagerRole(boolean byManagerRole) {
        this.byManagerRole = byManagerRole;
    }

    public Task.Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Task.Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    private boolean byEmployeeRole;
    private boolean byAssigned;
    private User.UserRole byUserRole;
    private boolean byManagerRole;
    private Task.Status taskStatus;
    private String EmployeeName;



}
