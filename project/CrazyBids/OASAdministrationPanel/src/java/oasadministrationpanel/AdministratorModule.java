/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.EmployeeControllerRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.EmployeeNotFoundException;


public class AdministratorModule {
    
    private EmployeeControllerRemote employeeControllerRemote;

    public AdministratorModule() {
    }

    public AdministratorModule(EmployeeControllerRemote employeeControllerRemote) {
        this.employeeControllerRemote = employeeControllerRemote;
    }
    
    public void taskMenu()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true)
        {
        System.out.println("*** OAS Aministration Panel - Administrator***\n");
        System.out.println("You are login as System Administrator\n");
        System.out.println("1: Create new employee");
        System.out.println("2: View employee details");
        System.out.println("3: Update employee details");
        System.out.println("4: Delete employee details");
        System.out.println("5: View all employee details");
        System.out.println("6: Back\n");
        response = 0;
        
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    createNewEmployee();
                }
                else if(response == 2)
                {
                    viewEmployeeDetails();
                }
                else if (response == 3)
                {
                    updateEmployeeDetails();
                }
                else if (response == 4)
                {
                    deleteEmployee();
                }
                else if (response == 5)
                {
                    viewAllEmployees();
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
    
    
    
    public void createNewEmployee()
    {
        
    }
    
    
    public void viewEmployeeDetails()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter Employee ID> ");
        Long employeeId = scanner.nextLong();

        try {
            Employee employee = employeeControllerRemote.retrieveEmployeeByEmployeeId(employeeId);
            System.out.println("View Employee " + employeeId);
            System.out.println("-----------------------------");
            System.out.println("Name: " + employee.getFirstName() + " " + employee.getLastName());
            System.out.println("Identification Number: " + employee.getIdentificationNumber());
            System.out.println("Contact Number: " + employee.getContactNumber());
            System.out.println("Address: " + employee.getAddress1() + ", S(" + employee.getPostalCode() + ")");
        }
        catch (EmployeeNotFoundException ex)
        {
            System.out.println("Employee does not exist: " + ex.getMessage());
        }
    }
    
    
    public void updateEmployeeDetails()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.print("Enter Employee ID> ");
        Long employeeId = scanner.nextLong();

        try {
            
            Employee employee = employeeControllerRemote.retrieveEmployeeByEmployeeId(employeeId);

            while (true)
            {
                System.out.println("1. Name");
                System.out.println("2. Contact Number");
                System.out.println("3. Address");
                System.out.println("4. Postal Code");
                System.out.println("5. Exit");

                System.out.print("Select option to update> ");
                response = scanner.nextInt();

                if (response == 1)
                {

                }
            }
        } catch (EmployeeNotFoundException ex)
        {
            System.out.println("Employee does not exist: " + ex.getMessage());
        }
        
    }
    
    
    public void deleteEmployee()
    {
        
    }
    
    
    public void viewAllEmployees()
    {
        
    }
    
}
