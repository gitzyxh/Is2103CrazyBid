/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Transaction;
import java.util.List;
import util.exception.TransactionNotFoundException;


public interface TransactionControllerRemote {
    
    public Transaction createNewTransaction(Transaction transaction);

    public List<Transaction> retrieveAllTransactions();

    public Transaction retrieveTransactionByTransactionId(Long transactionId) throws TransactionNotFoundException;
    
    public void setAuctionToTransaction(Long transactionId, Long auctionId);

    public void setCustomerToTransaction(Long transactionId, Long customerId);
    
    public void deleteTransaction(Long transactionId) throws TransactionNotFoundException;
    
}
