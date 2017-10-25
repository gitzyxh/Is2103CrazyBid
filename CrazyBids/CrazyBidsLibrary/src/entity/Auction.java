/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.AuctionStatus;


@Entity
public class Auction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    @Column(length = 256, nullable = false)
    private String description;
    @Column(nullable = true, precision = 18, scale = 4)
    private BigDecimal currentBid;
    @Column(nullable = true, precision = 18, scale = 4)
    private BigDecimal winningBid;
    @Column(nullable = true, precision = 18, scale = 4)
    private BigDecimal reservePrice;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;
    @Column(nullable = true)
    private Boolean enabled;
    @Column(length = 32, nullable = true)
    private String deliveryAddress;
    
    @ManyToOne
    private Employee employee;
    @ManyToMany
    private List<Customer> customers;
    @ManyToMany 
    private List<Transaction> transactions;

    public Auction() {
        customers = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public Auction(Date startDateTime, Date endDateTime, String description, BigDecimal currentBid, BigDecimal winningBid, BigDecimal reservePrice, AuctionStatus auctionStatus, Boolean enabled, String deliveryAddress) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.description = description;
        this.currentBid = currentBid;
        this.winningBid = winningBid;
        this.reservePrice = reservePrice;
        this.auctionStatus = auctionStatus;
        this.enabled = enabled;
        this.deliveryAddress = deliveryAddress;
    }


    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (auctionId != null ? auctionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Auction)) {
            return false;
        }
        Auction other = (Auction) object;
        if ((this.auctionId == null && other.auctionId != null) || (this.auctionId != null && !this.auctionId.equals(other.auctionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Auction[ id=" + auctionId + " ]";
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(BigDecimal currentBid) {
        this.currentBid = currentBid;
    }

    public BigDecimal getWinningBid() {
        return winningBid;
    }

    public void setWinningBid(BigDecimal winningBid) {
        this.winningBid = winningBid;
    }

    public AuctionStatus getAuctionStatus() {
        return auctionStatus;
    }

    public void setAuctionStatus(AuctionStatus auctionStatus) {
        this.auctionStatus = auctionStatus;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public BigDecimal getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(BigDecimal reservePrice) {
        this.reservePrice = reservePrice;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
}
