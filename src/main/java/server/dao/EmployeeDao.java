package server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import server.domain.Employee;

import java.util.Optional;

public interface EmployeeDao extends JpaRepository<Employee,String> {

     Optional<Employee> getEmployeeByFirstNameAndLastName(String FirstName, String LastName);
     Optional<Employee> findEmployeeByUsernameAndPassword(String providedUsername, String providedPassword);

}
