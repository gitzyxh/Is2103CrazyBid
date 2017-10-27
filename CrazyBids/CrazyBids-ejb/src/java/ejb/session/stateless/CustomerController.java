/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import entity.Customer;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AuctionNotFoundException;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;


@Stateless
@Local(CustomerControllerLocal.class)
@Remote(CustomerControllerRemote.class)
public class CustomerController implements CustomerControllerRemote, CustomerControllerLocal {

    @EJB
    private AuctionControllerLocal auctionControllerLocal;

    @PersistenceContext(unitName = "CrazyBids-ejbPU")
    private EntityManager em;

    public CustomerController() {
    }

    
    @Override
    public Customer createNewCustomer(Customer customer, Long auctionId) throws CustomerExistException, GeneralException
    {
        try
        {   
            em.persist(customer);
            em.flush();
            em.refresh(customer);

            return customer;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException"))
            {
                throw new CustomerExistException("Customer with same identification number already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    
    @Override
    public List<Customer> retrieveAllCustomers()
    {
        Query query = em.createQuery("SELECT c FROM Customer c");
        
        return query.getResultList();
    }
    
    
    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException
    {
        Customer customer = em.find(Customer.class, customerId);
        
        if (customer != null)
        {
            return customer;
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID " + customerId + " do not exist!");
        }
    }
    
    
    @Override
    public Customer retriveCustomerByIdentificationNumber(String identificationNumber) throws CustomerNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.identificationNumber = :inIdentificationNumber");
        query.setParameter("inIdentificationNumber", identificationNumber);
        
        try
        {
            return (Customer)query.getSingleResult();
        }
        catch (NoResultException | NonUniqueResultException ex)
        {
            throw new CustomerNotFoundException("Customer identification number " + identificationNumber + " do not exist!");
        }
    }
    
    
    @Override
    public Customer retrieveCustomerByUsername(String userName) throws CustomerNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.userName = :inUserName");
        query.setParameter("inUserName", userName);
        
        try 
        {
            return (Customer)query.getSingleResult();
        }
        catch (NoResultException | NonUniqueResultException ex)
        {
            throw new CustomerNotFoundException("Customer username " + userName + " do not exist!");
        }
    }
    
    
    
    @Override
    public void updateCustomer(Customer customer)
    {
        em.merge(customer);
    }
    
    
    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException
    {
        Customer customer = retrieveCustomerByCustomerId(customerId);
        
        if (customer != null) 
        {
            customer.getAuctions().remove(customer);
            customer.getAuctions().clear();
            
            em.remove(customer);
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID " + customerId + " do not exist!");
        }
    }
    
    
    @Override
    public Customer customerLogin(String userName, String password) throws InvalidLoginCredentialException, CustomerNotFoundException
    {
        try 
        {
            Customer customer = retrieveCustomerByUsername(userName);
            
            if(customer.getPassword().equals(password))
            {
                return customer;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password");
            }
        }
        catch (InvalidLoginCredentialException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password");
        }
    }
    
    
    @Override
    public void setAuctionToCustomer(Long customerId, Long auctionId)
    {
        try
        {
            Auction auction = auctionControllerLocal.retrieveAuctionByAuctionId(auctionId);
            Customer customer = retrieveCustomerByCustomerId(customerId);
            
            customer.getAuctions().add(auction);
            auction.getCustomers().add(customer);
        }
        catch (AuctionNotFoundException | CustomerNotFoundException ex)
        {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
}
