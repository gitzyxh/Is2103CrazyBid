/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;


@Stateless
@Local(EmployeeControllerLocal.class)
@Remote(EmployeeControllerRemote.class)

public class EmployeeController implements EmployeeControllerRemote, EmployeeControllerLocal {

    @PersistenceContext(unitName = "CrazyBids-ejbPU")
    private EntityManager em;
    
    @Override
    public Employee createNewEmployee(Employee employee) throws EmployeeExistException, GeneralException
    {
        try
        {
            em.persist(employee);
            em.flush();
            em.refresh(employee);

            return employee;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException"))
            {
                throw new EmployeeExistException("Employee with same username already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    
    @Override
    public List<Employee> retrieveAllEmployee()
    {
        Query query = em.createQuery("SELECT e FROM Employee e");
        
        return query.getResultList();
    }
    
    
    @Override
    public Employee retrieveEmployeeByEmployeeId (Long employeeId) throws EmployeeNotFoundException
    {
        Employee employee = em.find(Employee.class, employeeId);
        
        if(employee != null)
        {   
            return employee;
        }
        else
        {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " does not exist");
        }
    }
    
    
    @Override
    public Employee retrieveEmployeeByUsername(String userName) throws EmployeeNotFoundException
    {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.userName = :inUserName");
        query.setParameter("inUserName", userName);
        
        try 
        {
            return (Employee)query.getSingleResult();
        }
        catch (NoResultException | NonUniqueResultException ex)
        {
            throw new EmployeeNotFoundException("Employee username " + userName + " do not exist!");
        }
    }
    
    
    @Override
    public void updateEmployee(Employee employeeEntity)
    {
        em.merge(employeeEntity);
    }
    
    
    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException
    {
        Employee employeeToRemove = retrieveEmployeeByEmployeeId(employeeId);
        
        if (employeeToRemove != null)
        {
            em.remove(employeeToRemove);
        }
        else
        {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " do not exist!");
        }
    }
    
    
    @Override
    public Employee employeeLogin(String userName, String password) throws InvalidLoginCredentialException, EmployeeNotFoundException
    {
        try 
        {
            Employee employee = retrieveEmployeeByUsername(userName);
            
            if(employee.getPassword().equals(password))
            {
                return employee;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password");
            }
        }
        catch (InvalidLoginCredentialException | EmployeeNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password");
        }
    }
}
