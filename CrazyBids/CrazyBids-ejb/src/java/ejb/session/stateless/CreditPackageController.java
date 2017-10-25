/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import entity.Employee;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CreditPackageExistException;
import util.exception.CreditPackageNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;


@Stateless
@Local(CreditPackageControllerLocal.class)
@Remote(CreditPackageControllerRemote.class)
public class CreditPackageController implements CreditPackageControllerRemote, CreditPackageControllerLocal {

    @EJB
    private EmployeeControllerLocal employeeControllerLocal;

    @PersistenceContext(unitName = "CrazyBids-ejbPU")
    private EntityManager em;

    @Override
    public CreditPackage createNewCreditPackage(CreditPackage creditPackage, Long employeeId) throws CreditPackageExistException, GeneralException, EmployeeNotFoundException
    {
        try
        {
            Employee employee = employeeControllerLocal.retrieveEmployeeByEmployeeId(employeeId);
            
            em.persist(creditPackage);
            
            creditPackage.setEmployee(employee);
            employee.getCreditPackages().add(creditPackage);
            
            em.flush();
            em.refresh(creditPackage);

            return creditPackage;
        }
        catch(EmployeeNotFoundException ex)
        {
            throw new EmployeeNotFoundException("Unable to create new credit package as the employee record does not exist");
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException"))
            {
                throw new CreditPackageExistException("Credit Package with same amount already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    
    
    @Override
    public List<CreditPackage> retrieveAllCreditPackages()
    {
        Query query = em.createQuery("SELECT c FROM CreditPackage c");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public CreditPackage retrieveCreditPackageByCreditPackageId(Long creditPackageId) throws CreditPackageNotFoundException
    {
        CreditPackage creditPackage = em.find(CreditPackage.class, creditPackageId);
        
        if (creditPackage != null)
        {
            return creditPackage;
        }
        else
        {
            throw new CreditPackageNotFoundException("Credit Package ID " + creditPackageId + " do not exist!");
        }
    }
    
    
    @Override
    public void updateCreditPackage(CreditPackage creditPackage)
    {
        List<CreditPackage> creditPackages = retrieveAllCreditPackages();

        if (!creditPackages.contains(creditPackage.getCreditUnit()))
        {
            em.merge(creditPackage);
        }
        else
        {
            System.out.println("Credit package with credit unit " + creditPackage.getCreditUnit() + " exist!");
        }
    }

    
    @Override
    public void deleteCreditPackage(Long creditPackageId, Long employeeId) throws CreditPackageNotFoundException, EmployeeNotFoundException
    {
        CreditPackage creditPackage = retrieveCreditPackageByCreditPackageId(creditPackageId);
        Employee employee = employeeControllerLocal.retrieveEmployeeByEmployeeId(employeeId);
        
        if (creditPackage != null) 
        {
            creditPackage.setEmployee(null);
            employee.getAuctions().remove(creditPackage);
            em.remove(creditPackage);
        }
        else
        {
            throw new CreditPackageNotFoundException("Credit Package ID " + creditPackageId + " do not exist!");
        }
    }
    
}
