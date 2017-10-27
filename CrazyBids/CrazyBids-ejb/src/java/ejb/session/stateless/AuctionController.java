/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import entity.Employee;
import entity.Transaction;
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
import util.exception.AuctionExistException;
import util.exception.AuctionNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;


@Stateless
@Local(AuctionControllerLocal.class)
@Remote(AuctionControllerRemote.class)
public class AuctionController implements AuctionControllerRemote, AuctionControllerLocal {

    @EJB
    private EmployeeControllerLocal employeeControllerLocal;

    @PersistenceContext(unitName = "CrazyBids-ejbPU")
    private EntityManager em;

    
    @Override
    public Auction createNewAuction(Auction auction, Long employeeId) throws AuctionExistException, GeneralException, EmployeeNotFoundException
    {
        try
        {
            Employee employee = employeeControllerLocal.retrieveEmployeeByEmployeeId(employeeId);
     
            em.persist(auction);
            
            auction.setEmployee(employee);
            employee.getAuctions().add(auction);
            
            em.flush();
            em.refresh(auction);

            return auction;
        }
        catch(EmployeeNotFoundException ex)
        {
            throw new EmployeeNotFoundException("Unable to create new auction listing as the employee record does not exist");
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException"))
            {
                throw new AuctionExistException("Auction with same ID already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    
    @Override
    public List<Auction> retrieveAllAuction()
    {
        Query query = em.createQuery("SELECT a FROM Auction a");
        
        return query.getResultList();
    }
    
    @Override
    public List<Auction> retrieveAllAuctionBelowReservePrice(BigDecimal reservePrice)
    {
        List<Auction> auctionListing = retrieveAllAuction();
        List<Auction> auctionsBelowReservePrice = new ArrayList<>();
        
        for (Auction auction : auctionListing)
        {
            if (auction.getCurrentBid().compareTo(reservePrice) < 0)
            {
                auctionsBelowReservePrice.add(auction);
            }
        } 
        
        if (auctionsBelowReservePrice.isEmpty())
        {
            System.out.println("There is currently no auction listing below reserve price.");
        }
        
        return auctionsBelowReservePrice;
    }
    

    @Override
    public List<Auction> retrieveAllWonAuctions()
    {
        List<Auction> auctionListing = retrieveAllAuction();
        List<Auction> wonAuctions = new ArrayList<>();
        
        for (Auction auction : auctionListing)
        {
            if (auction.getWinningBid().doubleValue() > 0.0)
            {
                wonAuctions.add(auction);
            }
        }
        
        if (wonAuctions.isEmpty())
        {
            System.out.println("There is currently no winning auction.");
        }
        
        return wonAuctions;
    }
    
   
    @Override
    public Auction retrieveAuctionByAuctionId(Long auctionId) throws AuctionNotFoundException
    {
        Auction auction = em.find(Auction.class, auctionId);
        
        if (auction != null)
        {
            return auction;
        }
        else
        {
            throw new AuctionNotFoundException("Auction ID " + auctionId + " do not exist!");
        }
    }
    
    
    
    @Override
    public void updateAuction(Auction auction)
    {
        em.merge(auction);
    }
    
    

    @Override
    public void deleteAuction(Long auctionId)
    {
        try
        {
            Auction auction = retrieveAuctionByAuctionId(auctionId);
            em.remove(auction);
        } 
        catch (AuctionNotFoundException ex)
        {
            System.out.println("Auction with same ID already exist");
        }
    }
    
}
