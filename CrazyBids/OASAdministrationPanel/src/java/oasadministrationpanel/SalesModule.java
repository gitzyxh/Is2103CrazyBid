/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.AuctionControllerRemote;
import entity.Auction;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AuctionStatus;
import util.exception.AuctionExistException;
import util.exception.AuctionNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;


public class SalesModule {
    
    private AuctionControllerRemote auctionControllerRemote;

    public SalesModule() {
    }

    public SalesModule(AuctionControllerRemote auctionControllerRemote) {
        this.auctionControllerRemote = auctionControllerRemote;
    }

    
    public void taskMenu()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true)
        {
        System.out.println("*** OAS Aministration Panel - Sales Staff***\n");
        System.out.println("You are login as Sales Staff\n");
        System.out.println("1: Create new auction listing");
        System.out.println("2: View auction listing details");
        System.out.println("3: Update auction listing details");
        System.out.println("4: Delete auction listing");
        System.out.println("5: View all auction listings");
        System.out.println("6. View all auction listing with bids below reserve price");
        System.out.println("7: Back\n");
        response = 0;
        
            while(response < 1 || response > 7)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    createNewAuctionListing();
                }
                else if(response == 2)
                {
                    System.out.print("Enter auction ID> ");
                    viewAuctionListing(scanner.nextLong());
                }
                else if (response == 3)
                {
                    updateAuctionListing();
                }
                else if (response == 4)
                {
                    deleteAuctionListing();
                }
                else if (response == 5)
                {
                    viewAllAuctionListings();
                }
                else if (response == 6)
                {
                    viewAllAuctionListingBelowReservePrice();
                }
                else if (response == 7)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
        if(response == 7)
        {
            break;
        }
        }
    }
    
    public void createNewAuctionListing()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** OAS Aministration Panel - Sales Staff - Create New Auction Listing***\n");
        System.out.println("------------------------");
        
        try
        {
            Auction newAuction = new Auction();

            System.out.print("Enter Start Date Time> ");
            scanner.useDelimiter(",");
            String startDateTime = scanner.next();
            DateFormat formatter = new SimpleDateFormat("EEEE dd MMM yyyy");
            Date date = formatter.parse(startDateTime);
            newAuction.setStartDateTime(date);
            
            System.out.print("Enter End Date Time> ");
            scanner.useDelimiter(",");
            String endDateTime = scanner.next();
            date = formatter.parse(endDateTime);
            newAuction.setStartDateTime(date);
            
            System.out.print("Enter short description about the auction listing> ");
            newAuction.setDescription(scanner.nextLine().trim());
            
            //Assumes that all auction listing will be enabled
            newAuction.setEnabled(Boolean.TRUE);
            
            if(new Date().after(newAuction.getStartDateTime()))
            {
                newAuction.setAuctionStatus(AuctionStatus.OPEN);
            }
            else
            {
                newAuction.setAuctionStatus(AuctionStatus.CLOSED);
            }
            
            System.out.print("Enter employee ID> ");
            Long employeeId = scanner.nextLong();
            
            newAuction = auctionControllerRemote.createNewAuction(newAuction, employeeId);
            System.out.println("New auction listing created successfully!: " + newAuction.getAuctionId()+ "\n");
        } 
        catch (AuctionExistException | GeneralException | EmployeeNotFoundException | ParseException ex)
        {
            System.out.println("An error has occurred while creating the new credit package: " + ex.getMessage() + "!\n");
        }
    }
    
    
    public void viewAuctionListing(Long auctionId)
    {   
        System.out.println("*** OAS Aministration Panel - Sales Staff - View Auction Listing***\n");
        System.out.println("------------------------");
        
        try {
            Auction auction = auctionControllerRemote.retrieveAuctionByAuctionId(auctionId);
            System.out.printf("\n%12s%12s%12s%12s%12s%12s%12s%12s%12s" , "Auction ID", "Start Date Time", "End Date Time", "Description", "Current Bid", "Winning Bid", "Reserve Price", "Auction Status", "Enabled");
            System.out.printf("\n%12s%12s%12s%12s%12s%12s%12s%12s%12s", auction.getAuctionId(), auction.getStartDateTime(), auction.getEndDateTime(), auction.getDescription(), auction.getCurrentBid(), auction.getWinningBid(), auction.getReservePrice(), auction.getAuctionStatus(), auction.getEnabled());
        }
        catch (AuctionNotFoundException ex)
        {
            System.out.println("Auction ID does not exist: " + ex.getMessage());
        }
    }
    
    
    public void updateAuctionListing()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** OAS Aministration Panel - Sales Staff - Update Auction Listing***\n");
        System.out.println("------------------------");
        
        System.out.print("Enter Auction ID> ");
        Long auctionId = scanner.nextLong();
        
        try {
            
            Auction auction = auctionControllerRemote.retrieveAuctionByAuctionId(auctionId);

            System.out.println("1. Start Date Time");
            System.out.println("2. End Date Time");
            System.out.println("3. Description");
            System.out.println("4. Auction Status");
            System.out.println("5. Exit");

            System.out.print("Select option to update> ");
            response = scanner.nextInt();

            if (response < 5 && response > 0)
            {
                DateFormat formatter = new SimpleDateFormat("EEEE dd MMM yyyy");
                if (response == 1)
                {
                    System.out.print("Enter new start date time> ");
                    scanner.useDelimiter(",");
                    Date startDateTime = formatter.parse(scanner.next());
                    auction.setStartDateTime(startDateTime);
                }

                else if (response == 2)
                {
                    System.out.print("Enter new end date time> ");
                    scanner.useDelimiter(",");
                    Date endDateTime = formatter.parse(scanner.next());
                    auction.setEndDateTime(endDateTime);
                }

                else if (response == 3)
                {
                    System.out.print("Enter new description> ");
                    auction.setDescription(scanner.nextLine().trim());
                }

                else if (response == 4)
                {
                    System.out.println("1. Open");
                    System.out.println("2. Closed");
                    System.out.print("Select auction status> ");
                    if (scanner.nextLine().equals("1"))
                    {
                        auction.setAuctionStatus(AuctionStatus.OPEN);
                    }
                    else if (scanner.nextLine().equals("2"))
                    {
                        auction.setAuctionStatus(AuctionStatus.CLOSED);
                    }
                    else
                    {
                        System.out.println("Invalid option. Please try again!");
                    }
                }
                
                auctionControllerRemote.updateAuction(auction);
                System.out.println("Auction Listing details updated successfully!");
                viewAuctionListing(auctionId);
            }
            else if (response == 5)
            {
                taskMenu();
            }
            else 
            {
                System.out.println("Invalid option, please try again.");
            }
            
        } catch (AuctionNotFoundException | ParseException ex)
        {
            System.out.println("Error in updating auction listing: " + ex.getMessage());
        }
    }
    
    
    public void deleteAuctionListing()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** OAS Aministration Panel - Sales Staff - Delete Auction Listing***\n");
        System.out.println("------------------------");
        
        try
        {
            System.out.print("Enter auction ID> ");
            Long auctionId = scanner.nextLong();

            Auction auction = auctionControllerRemote.retrieveAuctionByAuctionId(auctionId);
            System.out.println("Confirm delete auction listing" + auction.getAuctionId() + " (Y/N)> ");
            String input = scanner.next().toUpperCase();
            
            if (input.equals("Y"))
            {
                viewAuctionListing(auctionId);
                auctionControllerRemote.deleteAuction(auctionId);
                System.out.println("Auction deleted successfully!");
            }
            else if (input.equals("N"))
            {
                System.out.println("No changes made to Auction.");
            }
            else
            {
                System.out.println("Invalid option. Please try again.");
            }
        }
        catch (AuctionNotFoundException ex)
        {
            System.out.println("Error in removing auction listing: " + ex.getMessage());
        }
    }
    
    
    public void viewAllAuctionListings()
    {
        System.out.println("*** OAS Aministration Panel - Sales Staff - View All Auction Listings***\n");
        System.out.println("------------------------");
        
        List<Auction> auctionListings = auctionControllerRemote.retrieveAllAuction();
        
        System.out.println("View All Auction Listing Details");
        System.out.println("-----------------------------");
        System.out.printf("\n%12s%12s%12s%12s%12s%12s%12s%12s%12s" , "Auction ID", "Start Date Time", "End Date Time", "Description", "Current Bid", "Winning Bid", "Reserve Price", "Auction Status", "Enabled");
        
        for (Auction auction : auctionListings)
        {
            System.out.printf("\n%12s%12s%12s%12s%12s%12s%12s%12s%12s", auction.getAuctionId(), auction.getStartDateTime(), auction.getEndDateTime(), auction.getDescription(), auction.getCurrentBid(), auction.getWinningBid(), auction.getReservePrice(), auction.getAuctionStatus(), auction.getEnabled());
        }
    }
    
    
    public void viewAllAuctionListingBelowReservePrice()
    {
        System.out.println("*** OAS Aministration Panel - Sales Staff - View All Auction Listings Below Reserve Price***\n");
        System.out.println("------------------------");
        
        List<Auction> auctionListings = auctionControllerRemote.retrieveAllAuction();
        
        System.out.println("View Auction Listing Details - Below Reserve Price");
        System.out.println("-----------------------------");
        System.out.printf("\n%12s%12s%12s%12s%12s%12s%12s%12s%12s" , "Auction ID", "Start Date Time", "End Date Time", "Description", "Current Bid", "Winning Bid", "Reserve Price", "Auction Status", "Enabled");
        
        for (Auction auction : auctionListings)
        {
            if (auction.getCurrentBid().compareTo(auction.getReservePrice()) < 0)
            {
                System.out.printf("\n%12s%12s%12s%12s%12s%12s%12s%12s%12s", auction.getAuctionId(), auction.getStartDateTime(), auction.getEndDateTime(), auction.getDescription(), auction.getCurrentBid(), auction.getWinningBid(), auction.getReservePrice(), auction.getAuctionStatus(), auction.getEnabled());
            }
        }
    }
    
}
