package server.dto;
import server.domain.Task;
import server.domain.User;

public class QueryParameterDTO {


    private boolean isStatus;

    public boolean isStatus() {
        return isStatus;
    }

    public void setStatus(boolean status) {
        isStatus = status;
    }

    public boolean isEmployeeRole() {
        return isEmployeeRole;
    }

    public void setEmployeeRole(boolean employeeRole) {
        isEmployeeRole = employeeRole;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public User.UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(User.UserRole userRole) {
        this.userRole = userRole;
    }

    public boolean isManagerRole() {
        return isManagerRole;
    }

    public void setManagerRole(boolean managerRole) {
        isManagerRole = managerRole;
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

    private boolean isEmployeeRole;
    private boolean isAssigned;
    private User.UserRole userRole;
    private boolean isManagerRole;


    private Task.Status taskStatus;
    private String EmployeeName;



}
