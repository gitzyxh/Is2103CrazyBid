/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CreditPackageExistException;
import util.exception.CreditPackageNotFoundException;
import util.exception.GeneralException;


@Stateless
@Local(CreditPackageControllerLocal.class)
@Remote(CreditPackageControllerRemote.class)
public class CreditPackageController implements CreditPackageControllerRemote, CreditPackageControllerLocal {

    @PersistenceContext(unitName = "CrazyBids-ejbPU")
    private EntityManager em;
    
    @Override
    public CreditPackage createNewCreditPackage(CreditPackage creditPackage) throws CreditPackageExistException, GeneralException
    {
        try
        {
            em.persist(creditPackage);
            em.flush();
            em.refresh(creditPackage);

            return creditPackage;
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
        em.merge(creditPackage);
    }

    
    @Override
    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException
    {
        CreditPackage creditPackage = retrieveCreditPackageByCreditPackageId(creditPackageId);
        
        if (creditPackage != null) 
        {
            em.remove(creditPackage);
        }
        else
        {
            throw new CreditPackageNotFoundException("Customer ID " + creditPackageId + " do not exist!");
        }
    }
    
}
