/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


@Entity
public class CreditPackage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditPackageId;
    @Column(nullable = true, precision = 18, scale = 4, unique = true)
    private BigDecimal creditUnit;
    
    @ManyToOne
    private Employee employee;
    @ManyToMany
    private List<Transaction> transactions;

    public CreditPackage() {
        transactions = new ArrayList<>();
    }

    public CreditPackage(BigDecimal creditUnit) {
        this.creditUnit = creditUnit;
    }

    public Long getCreditPackageId() {
        return creditPackageId;
    }

    public void setCreditPackageId(Long creditPackageId) {
        this.creditPackageId = creditPackageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditPackageId != null ? creditPackageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditPackage)) {
            return false;
        }
        CreditPackage other = (CreditPackage) object;
        if ((this.creditPackageId == null && other.creditPackageId != null) || (this.creditPackageId != null && !this.creditPackageId.equals(other.creditPackageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditPackage[ id=" + creditPackageId + " ]";
    }

    public BigDecimal getCreditUnit() {
        return creditUnit;
    }

    public void setCreditUnit(BigDecimal creditUnit) {
        this.creditUnit = creditUnit;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
}
