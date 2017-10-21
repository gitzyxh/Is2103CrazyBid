/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.AuctionControllerRemote;


public class SalesModule {
    
    private AuctionControllerRemote auctionControllerRemote;

    public SalesModule() {
    }

    public SalesModule(AuctionControllerRemote auctionControllerRemote) {
        this.auctionControllerRemote = auctionControllerRemote;
    }

    
    public void taskMenu()
    {
        
    }
    
}
