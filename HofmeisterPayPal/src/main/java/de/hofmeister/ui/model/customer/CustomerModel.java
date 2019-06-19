package de.hofmeister.ui.model.customer;

import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.service.CustomerService.CustomerService;
import de.hofmeister.service.InitService.InitService;
import de.hofmeister.ui.model.transaction.TransactionsModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ManagedBean(name = "custModel")
@SessionScoped
public class CustomerModel {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private InitService initService;

    @Inject
    private CustomerService customerService;

    private String email;
    private String pwd;
    private String firstName;
    private String secondName;

    private CustomerAccount account; // if this attribute is set, the user is logged in successfully

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

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

    public CustomerAccount getAccount() {
        return account;
    }
    public void setAccount(CustomerAccount account) {
        this.account = account;
    }

    public String login(String page, CustomerAccountModel customerAccountModel, TransactionsModel transactionsModel){
        initService.init();
        Customer customer = new Customer();
        customer.setEmailAddress(this.getEmail());
        customer.setPassword(this.getPwd());
        try{
            CustomerAccount acc = customerService.login(customer);
            customerAccountModel.init();
            customerAccountModel.setCreditInEuro(String.valueOf(((double)acc.getCreditInEuroCent())/100));
            customerAccountModel.setBankAccounts(acc.getBankAccounts());
            customerAccountModel.setContacts(acc.getContacts());
            customerAccountModel.setDefaultBankAccount(acc.getDefaultBankAccount());
            customerAccountModel.setDirectMoneyTransferAllowed(acc.isDirectMoneyTransferAllowed());
            this.setAccount(acc);
            transactionsModel.init(this);
            customer = customerService.getCustomerByEmail(customer.getEmailAddress());
            this.setFirstName(customer.getFirstName());
            this.setSecondName(customer.getSecondName());
        }
        catch(CustomerService.HofmeisterCustomerServiceException ex){
            return "pages/home/login-failed";
        }

        if(page.equals("index")){
            return "pages/home/home";
        }
        else{
            return "home";
        }

    }

}