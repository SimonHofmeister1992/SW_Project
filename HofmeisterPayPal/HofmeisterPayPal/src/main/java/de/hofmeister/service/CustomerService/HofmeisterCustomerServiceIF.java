package de.hofmeister.service.CustomerService;

import de.hofmeister.entity.bank.BankAccount;
import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.entity.transaction.Transactions;

public interface HofmeisterCustomerServiceIF {
    CustomerAccount login(Customer customer) throws CustomerService.HofmeisterCustomerServiceException;
    Transactions chargeMoney(Transactions transactions) throws ITransactionException;
    void addBankAccount(CustomerAccount customer, BankAccount bankAccount) throws CustomerService.HofmeisterCustomerServiceException;
    void addContact(CustomerAccount customer, Customer contact) throws CustomerService.HofmeisterCustomerServiceException;
}
