/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Transaction;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.TransactionNotFoundException;


@Stateless
@Local(TransactionControllerLocal.class)
@Remote(TransactionControllerRemote.class)

public class TransactionController implements TransactionControllerRemote, TransactionControllerLocal {

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
    
}
