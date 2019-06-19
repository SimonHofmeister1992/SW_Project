package de.hofmeister.ui.model.bank;

import de.hofmeister.entity.bank.BankAccount;
import de.hofmeister.entity.bank.FinanceInstitute;
import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.service.CustomerService.CustomerService;
import de.hofmeister.ui.converter.BankAccountConverter;
import de.hofmeister.ui.model.customer.CustomerAccountModel;
import de.hofmeister.ui.model.customer.CustomerModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.validation.ValidationException;
import java.util.List;

@ManagedBean(name = "bankAccountModel")
@RequestScoped
public class BankAccountModel {

    @Inject
    CustomerService customerService;

    private String BIC;
    private String nameOfFinanceInstitute;
    private String IBAN;
    private boolean defaultBankAccount;
    private boolean directTransferAllowed;
    private String loginId;
    private String loginPwd;

    public boolean isDefaultBankAccount() {
        return defaultBankAccount;
    }
    public void setDefaultBankAccount(boolean defaultBankAccount) {
        this.defaultBankAccount = defaultBankAccount;
    }

    public String getIBAN() {
        return IBAN;
    }
    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getNameOfFinanceInstitute() {
        return nameOfFinanceInstitute;
    }
    public void setNameOfFinanceInstitute(String nameOfFinanceInstitute) {
        this.nameOfFinanceInstitute = nameOfFinanceInstitute;
    }

    public String getBIC() {
        return BIC;
    }
    public void setBIC(String BIC) {
        this.BIC = BIC;
    }

    public boolean isDirectTransferAllowed() {
        return directTransferAllowed;
    }
    public void setDirectTransferAllowed(boolean directTransferAllowed) {
        this.directTransferAllowed = directTransferAllowed;
    }

    public String getLoginId() {
        return loginId;
    }
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPwd() {
        return loginPwd;
    }
    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String addBankAccount(CustomerModel customerModel, CustomerAccountModel customerAccountModel){
        CustomerAccount customerAccountSelf= null;
        try {
            Customer customerSelf = customerService.getCustomerByEmail(customerModel.getEmail());
            customerAccountSelf = customerService.getCustomerAccountByCustomer(customerSelf);
        } catch (CustomerService.HofmeisterCustomerServiceException e) {
            // shall not be reachable for logged-in user
        }

        if(customerAccountSelf != null){

            FinanceInstitute financeInstitute = new FinanceInstitute();
            financeInstitute.setBic(this.getBIC());
            financeInstitute.setName(this.getNameOfFinanceInstitute());

            BankAccount bankAccount = new BankAccount();
            try{
                bankAccount.setIban(this.getIBAN());
            }
            catch (ValidationException ex){
                return "/pages/bankaccount/bankaccount-add-failed";
            }
            bankAccount.setFinanceInstitute(financeInstitute);

            if(this.isDefaultBankAccount()){
                customerAccountModel.setDefaultBankAccount(bankAccount);
                customerAccountModel.setDirectMoneyTransferAllowed(this.isDirectTransferAllowed());
                customerAccountSelf.setDefaultBankAccount(bankAccount);
                customerAccountSelf.setDirectMoneyTransferAllowed(this.isDirectTransferAllowed());
                if(this.getLoginId()==null || this.getLoginId().equals("")) return "/pages/bankaccount/bankaccount-add-failed";
                if(this.getLoginPwd()==null || this.getLoginPwd().equals("")) return "/pages/bankaccount/bankaccount-add-failed";
                bankAccount.setBankLoginId(this.getLoginId());
                bankAccount.setBankLoginPwd(this.getLoginPwd());
            }

            try {
                customerService.addBankAccount(customerAccountSelf, bankAccount);
                List<BankAccount> bankAccounts = customerAccountModel.getBankAccounts();
                boolean alreadyContainsBankAccount=false;
                for (BankAccount bA : bankAccounts){
                    if(bA.getIban().equals(bankAccount.getIban())){
                        alreadyContainsBankAccount = true;
                    }
                }
                if(!alreadyContainsBankAccount) {
                    bankAccounts.add(bankAccount);
                    BankAccountConverter bankAccountConverter = customerAccountModel.getBankAccountConverter();
                    bankAccountConverter.addBankAccount(bankAccount);
                }
                customerAccountModel.setBankAccounts(bankAccounts);
                
                return "/pages/bankaccount/bankaccounts";
            } catch (CustomerService.HofmeisterCustomerServiceException e) {
                return "/pages/bankaccount/bankaccount-add-failed";
            }

        }
        return "/index"; // customer is not registered, ask for login
    }
}
