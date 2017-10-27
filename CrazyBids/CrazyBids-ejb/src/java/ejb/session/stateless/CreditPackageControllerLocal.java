/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import java.util.List;
import util.exception.CreditPackageExistException;
import util.exception.CreditPackageNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;


public interface CreditPackageControllerLocal {

    public CreditPackage retrieveCreditPackageByCreditPackageId(Long creditPackageId) throws CreditPackageNotFoundException;

    public void updateCreditPackage(CreditPackage creditPackage);

    public List<CreditPackage> retrieveAllCreditPackages();

    public CreditPackage createNewCreditPackage(CreditPackage creditPackage, Long employeeId) throws CreditPackageExistException, GeneralException, EmployeeNotFoundException;

    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException;

    
    
}
