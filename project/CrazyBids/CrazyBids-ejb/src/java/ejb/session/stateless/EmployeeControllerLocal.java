/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

public interface EmployeeControllerLocal {

    public Employee createNewEmployee(Employee employee) throws EmployeeExistException, GeneralException;

    public List<Employee> retrieveAllEmployee();

    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;

    public Employee retrieveEmployeeByUsername(String userName) throws EmployeeNotFoundException;

    public void updateEmployee(Employee employeeEntity);

    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;

    public Employee employeeLogin(String userName, String password) throws InvalidLoginCredentialException, EmployeeNotFoundException;
    
}
