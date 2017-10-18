/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import util.exception.CreditPackageExistException;
import util.exception.CreditPackageNotFoundException;
import util.exception.GeneralException;


public interface CreditPackageControllerLocal {

    public CreditPackage createNewCreditPackage(CreditPackage creditPackage) throws CreditPackageExistException, GeneralException;

    public CreditPackage retrieveCreditPackageByCreditPackageId(Long creditPackageId) throws CreditPackageNotFoundException;

    public void updateCreditPackage(CreditPackage creditPackage);

    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException;
    
}
