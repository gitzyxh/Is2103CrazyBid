/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Auction;
import java.math.BigDecimal;
import java.util.List;
import util.exception.AuctionExistException;
import util.exception.AuctionNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;


public interface AuctionControllerLocal {

    public List<Auction> retrieveAllAuction();

    public List<Auction> retrieveAllAuctionBelowReservePrice(BigDecimal reservePrice);

    public List<Auction> retrieveAllWonAuctions();

    public Auction retrieveAuctionByAuctionId(Long auctionId) throws AuctionNotFoundException;

    public void updateAuction(Auction auction);

    public Auction createNewAuction(Auction auction, Long employeeId) throws AuctionExistException, GeneralException, EmployeeNotFoundException;

    public void deleteAuction(Long auctionId) throws AuctionNotFoundException;
    
}
