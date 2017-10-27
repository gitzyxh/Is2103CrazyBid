/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import entity.Customer;
import entity.Transaction;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AuctionNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.TransactionNotFoundException;


@Stateless
@Local(TransactionControllerLocal.class)
@Remote(TransactionControllerRemote.class)

public class TransactionController implements TransactionControllerRemote, TransactionControllerLocal {

    @EJB
    private CustomerControllerLocal customerControllerLocal;
    @EJB
    private AuctionControllerLocal auctionControllerLocal;

    @PersistenceContext(unitName = "CrazyBids-ejbPU")
    private EntityManager em;
    
    
    @Override
    public Transaction createNewTransaction(Transaction transaction) 
    {
        em.persist(transaction);
        em.flush();
        em.refresh(transaction);

        return transaction;
    }
    
    

    @Override
    public List<Transaction> retrieveAllTransactions()
    {
        Query query = em.createQuery("SELECT t FROM Transaction t");
        
        return query.getResultList();
    }
    
    

    @Override
    public Transaction retrieveTransactionByTransactionId(Long transactionId) throws TransactionNotFoundException
    {
        Transaction transaction = em.find(Transaction.class, transactionId);
        
        if (transaction != null)
        {
            return transaction;
        }
        else
        {
            throw new TransactionNotFoundException("Transaction ID " + transactionId + " do not exist!");
        }
    }
    
    
    @Override
    public void setAuctionToTransaction(Long transactionId, Long auctionId)
    {
        try
        {
            Transaction transaction = retrieveTransactionByTransactionId(transactionId);
            Auction auction = auctionControllerLocal.retrieveAuctionByAuctionId(auctionId);
            
            transaction.setAuction(auction);
            auction.getTransactions().add(transaction);
        }
        catch (TransactionNotFoundException | AuctionNotFoundException ex)
        {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    
    @Override
    public void setCustomerToTransaction(Long transactionId, Long customerId)
    {
        try
        {
            Transaction transaction = retrieveTransactionByTransactionId(transactionId);
            Customer customer = customerControllerLocal.retrieveCustomerByCustomerId(customerId);
            
            transaction.setCustomer(customer);
            customer.getTransactions().add(transaction);
        }
        catch (TransactionNotFoundException | CustomerNotFoundException ex)
        {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    
    @Override
    public void deleteTransaction(Long transactionId) throws TransactionNotFoundException
    {
        Transaction transaction = retrieveTransactionByTransactionId(transactionId);
        
        if (transaction != null) 
        {
            transaction.getCustomer().getTransactions().remove(transaction);
            transaction.getAuction().getTransactions().remove(transaction);
            
            em.remove(transaction);
        }
        else
        {
            throw new TransactionNotFoundException("Transaction ID " + transactionId + " do not exist!");
        }
    }
}
