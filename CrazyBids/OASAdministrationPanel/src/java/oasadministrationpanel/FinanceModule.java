/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.CreditPackageControllerRemote;
import entity.CreditPackage;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import util.exception.CreditPackageExistException;
import util.exception.CreditPackageNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;


public class FinanceModule {
    
    private CreditPackageControllerRemote creditPackageControllerRemote;

    public FinanceModule() {
    }

    public FinanceModule(CreditPackageControllerRemote creditPackageControllerRemote) {
        this.creditPackageControllerRemote = creditPackageControllerRemote;
    }
    
    
    public void taskMenu()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true)
        {
        System.out.println("*** OAS Aministration Panel - Finance Staff***\n");
        System.out.println("You are login as Finance Staff\n");
        System.out.println("1: Create new credit package");
        System.out.println("2: View credit package details");
        System.out.println("3: Update credit package details");
        System.out.println("4: Delete credit package");
        System.out.println("5: View all credit packages");
        System.out.println("6: Back\n");
        response = 0;
        
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    createNewCreditPackage();
                }
                else if(response == 2)
                {
                    System.out.print("Enter credit package ID> ");
                    viewCreditPackage(scanner.nextLong());
                }
                else if (response == 3)
                {
                    updateCreditPackage();
                }
                else if (response == 4)
                {
                    deleteCreditPackage();
                }
                else if (response == 5)
                {
                    viewAllCreditPackages();
                }
                else if (response == 6)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
        if(response == 6)
        {
            break;
        }
        }
    }
    
    
    public void createNewCreditPackage()
    {
        System.out.println("*** OAS Aministration Panel - Finance Staff - Create New Credit Package***\n");
        System.out.println("------------------------");
        
        try
        {
            Scanner scanner = new Scanner(System.in);
            CreditPackage creditPackage = new CreditPackage();

            System.out.print("Enter Credit Unit> ");
            creditPackage.setCreditUnit(scanner.nextBigDecimal());
            
            System.out.print("Enter Employee ID> ");
            Long employeeId = scanner.nextLong();

            creditPackage = creditPackageControllerRemote.createNewCreditPackage(creditPackage, employeeId);
            System.out.println("New credit package created successfully!: " + creditPackage.getCreditPackageId()+ "\n");
        } 
        catch (CreditPackageExistException | GeneralException | EmployeeNotFoundException ex)
        {
            System.out.println("An error has occurred while creating the new credit package: " + ex.getMessage() + "!\n");
        }
    }
    
    
    public void viewCreditPackage(Long creditPackageId)
    {
        System.out.println("*** OAS Aministration Panel - Finance Staff - View Credit Package***\n");
        System.out.println("------------------------");
        
        try {
            CreditPackage creditPackage = creditPackageControllerRemote.retrieveCreditPackageByCreditPackageId(creditPackageId);
            System.out.println("View Credit Package " + creditPackageId + ": ");
            System.out.println("-----------------------------");
            System.out.println("Credit Package ID: " + creditPackage.getCreditPackageId());
            System.out.println("Credit Unit: " + creditPackage.getCreditUnit());
        }
        catch (CreditPackageNotFoundException ex)
        {
            System.out.println("Credit Package does not exist: " + ex.getMessage());
        }
    }
    
    
    public void updateCreditPackage()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** OAS Aministration Panel - Finance Staff - Update Credit Package***\n");
        System.out.println("------------------------");
 
        System.out.print("Enter Credit Package ID> ");
        Long creditPackageId = scanner.nextLong();

        try { 
            CreditPackage creditPackage = creditPackageControllerRemote.retrieveCreditPackageByCreditPackageId(creditPackageId);
            System.out.print("Enter new credit unit> ");
            BigDecimal newCreditUnit = scanner.nextBigDecimal();
            
            creditPackage.setCreditUnit(newCreditUnit);
            creditPackageControllerRemote.updateCreditPackage(creditPackage);
            
            viewCreditPackage(creditPackageId);
        }
        catch (CreditPackageNotFoundException ex)
        {
            System.out.println("Credit Package does not exist: " + ex.getMessage());
        }
    }
    
    
    public void deleteCreditPackage()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** OAS Aministration Panel - Finance Staff - Delete Credit Package***\n");
        System.out.println("------------------------");
        
        try
        {
            System.out.print("Enter Credit Package ID> ");
            Long creditPackageId = scanner.nextLong();

            CreditPackage creditPackage = creditPackageControllerRemote.retrieveCreditPackageByCreditPackageId(creditPackageId);
            System.out.println("Confirm delete Credit Package " + creditPackage.getCreditPackageId() + " (Y/N)> ");
            String input = scanner.next().toUpperCase();
            
            if (input.equals("Y"))
            {
                System.out.print("Enter Employee ID> ");
                Long employeeId = scanner.nextLong();
                viewCreditPackage(creditPackageId);
                creditPackageControllerRemote.deleteCreditPackage(creditPackageId);
                System.out.println("Credit Package deleted successfully!");
            }
            else if (input.equals("N"))
            {
                System.out.println("No changes made to employee.");
            }
            else
            {
                System.out.println("Invalid option. Please try again.");
            }
        }
        catch (CreditPackageNotFoundException ex)
        {
            System.out.println("Credit Package does not exist!");
        }
    }
    
    
    public void viewAllCreditPackages()
    {
        System.out.println("*** OAS Aministration Panel - Finance Staff - View All Credit Packages***\n");
        System.out.println("------------------------");
        
        List<CreditPackage> creditPackages = creditPackageControllerRemote.retrieveAllCreditPackages();
        
        System.out.println("View All Credit Package Details");
        System.out.println("-----------------------------");
        System.out.printf("\n%3s%12s", "ID", "Credit Unit");
       
        for (CreditPackage creditPackage : creditPackages)
        {
            System.out.printf("\n%3s%12s", creditPackage.getCreditPackageId(), creditPackage.getCreditUnit());
        }
    }
}
