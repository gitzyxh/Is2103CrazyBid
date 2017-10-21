/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.AuctionControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import javax.ejb.EJB;


public class Main {

    @EJB
    private static CreditPackageControllerRemote creditPackageControllerRemote;
    @EJB
    private static AuctionControllerRemote auctionControllerRemote;
    @EJB
    private static EmployeeControllerRemote employeeControllerRemote;
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(creditPackageControllerRemote, auctionControllerRemote, employeeControllerRemote);
        mainApp.runApp();
    }
    
}
