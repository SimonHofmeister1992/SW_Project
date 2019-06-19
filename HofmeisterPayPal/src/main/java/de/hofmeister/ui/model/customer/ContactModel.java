package de.hofmeister.ui.model.customer;

import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.service.CustomerService.CustomerService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@ManagedBean(name = "contactModel")
@RequestScoped
public class ContactModel {

    @Inject
    private CustomerService customerService;

    private String firstName;
    private String secondName;
    private String email;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String addContact(CustomerModel customerModel, CustomerAccountModel customerAccountModel){
        try {
            Customer contact = customerService.getCustomerByEmail(this.getEmail());
            if(!contact.getFirstName().equals(this.getFirstName())) return "/pages/customer/contact-add-failed";
            if(!contact.getSecondName().equals(this.getSecondName())) return "/pages/customer/contact-add-failed";

            Customer customerSelf = customerService.getCustomerByEmail(customerModel.getEmail());
            CustomerAccount customerAccountSelf = customerService.getCustomerAccountByCustomer(customerSelf);

            customerService.addContact(customerAccountSelf,contact);
            List<Customer> contacts = customerAccountModel.getContacts();
            boolean alreadyContainsContact = false;
            for (Customer customer : contacts){
                if(customer.getEmailAddress().equals(contact.getEmailAddress())){
                    alreadyContainsContact = true;
                }
            }
            if(!alreadyContainsContact){
            contacts.add(contact);
            customerAccountModel.setContacts(contacts);
            }

            return "/pages/customer/contacts";

        } catch (CustomerService.HofmeisterCustomerServiceException e) {
            return "/pages/customer/contact-add-failed";
        }
    }
}
