/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;


public interface CustomerControllerLocal {

    public Customer createNewCustomer(Customer customer) throws CustomerExistException, GeneralException;

    public List<Customer> retrieveAllCustomers();

    public Customer retriveCustomerByIdentificationNumber(String identificationNumber) throws CustomerNotFoundException;

    public void updateCustomer(Customer customer);

    public void deleteCustomer(Long customerId) throws CustomerNotFoundException;

    public Customer customerLogin(String userName, String password) throws InvalidLoginCredentialException, CustomerNotFoundException;

    public Customer retrieveCustomerByUsername(String userName) throws CustomerNotFoundException;

    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;
    
}
