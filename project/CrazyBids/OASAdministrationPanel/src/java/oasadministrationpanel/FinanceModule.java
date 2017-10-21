/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.CreditPackageControllerRemote;


public class FinanceModule {
    
    private CreditPackageControllerRemote creditPackageControllerRemote;

    public FinanceModule() {
    }

    public FinanceModule(CreditPackageControllerRemote creditPackageControllerRemote) {
        this.creditPackageControllerRemote = creditPackageControllerRemote;
    }
    
    
    
    public void taskMenu()
    {
        
    }
    
}
