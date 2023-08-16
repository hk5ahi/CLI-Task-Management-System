package server.service;

import server.domain.Employee;
import server.dto.TaskDTO;


import java.util.List;

public interface EmployeeService {


    void updateTotalTime(TaskDTO taskDTO, Employee employee);



}
