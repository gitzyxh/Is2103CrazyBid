/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasadministrationpanel;

import ejb.session.stateless.EmployeeControllerRemote;
import entity.Employee;
import java.util.List;
import java.util.Scanner;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;


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
                    System.out.print("Enter Employee ID> ");
                    Long employeeId = scanner.nextLong();
                    viewEmployeeDetails(employeeId);
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
        System.out.println("*** OAS Aministration Panel - Administrator - Create New Employee***\n");
        System.out.println("------------------------\n");
        
        try
        { 
            Scanner scanner = new Scanner(System.in);
            Employee employee = new Employee();

            System.out.print("Enter First Name> ");
            employee.setFirstName(scanner.nextLine().trim());
            System.out.print("Enter Last Name> ");
            employee.setLastName(scanner.nextLine().trim());
            System.out.print("Enter Identification Number> ");
            employee.setIdentificationNumber(scanner.nextLine().trim());
            System.out.print("Enter Contact Number> ");
            employee.setContactNumber(scanner.nextLine().trim());
            System.out.print("Enter Address Line 1> ");
            employee.setAddress1(scanner.nextLine().trim());
            System.out.print("Enter Postal Code> ");
            employee.setPostalCode(scanner.nextLine().trim());

            employee = employeeControllerRemote.createNewEmployee(employee);
            System.out.println("New employee created successfully!: " + employee.getEmployeeId() + "\n");
        }
        catch(EmployeeExistException | GeneralException ex)
        {
            System.out.println("An error has occurred while creating the new employee: " + ex.getMessage() + "!\n");
        }
    }
    
    
    public void viewEmployeeDetails(Long employeeId)
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** OAS Aministration Panel - Administrator - View Employee Details***\n");
        System.out.println("------------------------\n");

        try {
            Employee employee = employeeControllerRemote.retrieveEmployeeByEmployeeId(employeeId);
            System.out.println("View Employee " + employeeId + ": ");
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
        
        System.out.println("*** OAS Aministration Panel - Administrator -  Update Employee Details***\n");
        System.out.println("------------------------\n");
        
        System.out.print("Enter Employee ID> ");
        Long employeeId = scanner.nextLong();

        try {
            
            Employee employee = employeeControllerRemote.retrieveEmployeeByEmployeeId(employeeId);

            System.out.println("1. Name");
            System.out.println("2. Contact Number");
            System.out.println("3. Address");
            System.out.println("4. Postal Code");
            System.out.println("5. Exit");

            System.out.print("Select option to update> ");
            response = scanner.nextInt();

            if (response < 5 && response > 0)
            {
                if (response == 1)
                {
                    System.out.print("Enter new first name> ");
                    employee.setFirstName(scanner.nextLine().trim());
                    System.out.print("Enter new last name> ");
                    employee.setLastName(scanner.nextLine().trim());
                }

                else if (response == 2)
                {
                    System.out.print("Enter new contact number> ");
                    employee.setContactNumber(scanner.nextLine().trim());
                }

                else if (response == 3)
                {
                    System.out.print("Enter new address> ");
                    employee.setAddress1(scanner.nextLine().trim());
                }

                else if (response == 4)
                {
                    System.out.print("Enter new postal code> ");
                    employee.setPostalCode(scanner.nextLine().trim());
                }
                
                employeeControllerRemote.updateEmployee(employee);
                System.out.println("Employee details updated successfully!");
                viewEmployeeDetails(employeeId);
            }
            else if (response == 5)
            {
                taskMenu();
            }
            else 
            {
                System.out.println("Invalid option, please try again.");
            }
            
        } catch (EmployeeNotFoundException ex)
        {
            System.out.println("Employee does not exist: " + ex.getMessage());
        }
        
    }
    
    
    public void deleteEmployee()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** OAS Aministration Panel - Administrator - Delete Employee***\n");
        System.out.println("------------------------\n");
        
        try
        {
            System.out.print("Enter employee ID> ");
            Long employeeId = scanner.nextLong();

            Employee employee = employeeControllerRemote.retrieveEmployeeByEmployeeId(employeeId);
            System.out.println("Confirm delete employee " + employee.getFirstName() + " " + employee.getLastName() + " (Y/N)> ");
            String input = scanner.next().toUpperCase();
            
            if (input.equals("Y"))
            {
                viewEmployeeDetails(employeeId);
                employeeControllerRemote.deleteEmployee(employeeId);
                System.out.println("Employee deleted successfully!");
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
        catch (EmployeeNotFoundException ex)
        {
            System.out.println("Employee does not exist: " + ex.getMessage());
        }
    }
    
    
    public void viewAllEmployees()
    {   
        System.out.println("*** OAS Aministration Panel - Administrator - View All Employees***\n");
        System.out.println("------------------------\n");
        
        List<Employee> employees = employeeControllerRemote.retrieveAllEmployee();
       
        for (Employee employee : employees)
        {
            System.out.println("View All Employee Details");
            System.out.println("-----------------------------");
            System.out.printf("");
            System.out.println("Name: " + employee.getFirstName() + " " + employee.getLastName());
            System.out.println("Identification Number: " + employee.getIdentificationNumber());
            System.out.println("Contact Number: " + employee.getContactNumber());
            System.out.println("Address: " + employee.getAddress1() + ", S(" + employee.getPostalCode() + ")");
        }
    }
    
}
