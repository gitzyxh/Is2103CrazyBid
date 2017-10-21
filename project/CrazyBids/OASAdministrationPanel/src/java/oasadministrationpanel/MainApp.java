/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.AuctionControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;


public class MainApp {
    
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private AuctionControllerRemote auctionControllerRemote;
    private EmployeeControllerRemote employeeControllerRemote;

    private AdministratorModule administratorModule;
    private FinanceModule financeModule;
    private SalesModule salesModule;
    
    public MainApp() 
    {
    }
    
    public MainApp(CreditPackageControllerRemote creditPackageControllerRemote, AuctionControllerRemote auctionControllerRemote, EmployeeControllerRemote employeeControllerRemote)
    {
        this.creditPackageControllerRemote = creditPackageControllerRemote;
        this.auctionControllerRemote = auctionControllerRemote;
        this.employeeControllerRemote = employeeControllerRemote; 
    }
    
    
    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Crazy Bids - OAS Administration Panel ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doLogin();
                    administratorModule = new AdministratorModule(employeeControllerRemote);
                    financeModule = new FinanceModule(creditPackageControllerRemote);
                    salesModule = new SalesModule(auctionControllerRemote);
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
    
    private void doLogin()
    {
        Scanner scanner = new Scanner(System.in);
        String username;
        String password = "";
        
        System.out.println("*** OAS Administration :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            try 
            {
                Employee employee = employeeControllerRemote.employeeLogin(username, password);
                mainMenu(employee);
            }
            catch (EmployeeNotFoundException ex)
            {
                System.out.println("Employee do not exist. Please try again!");
            }
            catch (InvalidLoginCredentialException ex)
            {
                System.out.println("Invalid login credential. Please try again!");
            }
        }
        else
        {
            System.out.println("Login credential was not provided!");
        }
    }
    
    
    private void mainMenu(Employee employee)
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** OAS Aministration Panel ***\n");
            System.out.println("You are login\n");
            System.out.println("1: Change Password");
            System.out.println("2: Access Task Menu");
            System.out.println("3: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doChangePassword();
                }
                else if(response == 2)
                {
                    if (employee.getAccessRight().equals("ADMINISTRATOR"))
                    {
                        administratorModule.taskMenu();
                    }
                    else if (employee.getAccessRight().equals("FINANCE"))
                    {
                        financeModule.taskMenu();
                    }
                    else
                    {
                        salesModule.taskMenu();
                    }
                }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
        if(response == 4)
        {
            break;
        }
        }
    }
    

    private void doChangePassword()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter employee ID> ");
            Long employeeId = scanner.nextLong();
            System.out.print("Enter current password> ");
            String currentPassword = scanner.nextLine().trim();
            System.out.print("Enter new password> ");
            String newPassword = scanner.nextLine().trim();
            System.out.print("Enter new PIN again> ");
            String reenterNewPassword = scanner.nextLine().trim();
            if(newPassword.equals(reenterNewPassword))
            {
                employeeControllerRemote.changePassword(employeeId, currentPassword, newPassword);
                System.out.println("New PIN changed successfully!\n");
            }
            else
            {
                System.out.println("New PIN mismatched!\n");
            }
        } 
        catch (EmployeeNotFoundException ex)
        {
            System.out.println("Employee ID do not exist!");
        }
    }

}
