package de.hofmeister.ui.model.customer;

import de.hofmeister.entity.bank.BankAccount;
import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.transaction.Transactions;
import de.hofmeister.service.CustomerService.CustomerService;
import de.hofmeister.service.CustomerService.ITransactionException;
import de.hofmeister.ui.converter.BankAccountConverter;
import de.hofmeister.ui.converter.CustomerConverter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "custAccModel")
@SessionScoped
public class CustomerAccountModel {

    @Inject
    private CustomerService customerService;

    @Inject
    private BankAccountConverter bankAccountConverter;

    @Inject
    private CustomerConverter customerConverter;

    private String creditInEuro;
    private List<BankAccount> bankAccounts;
    private List<Customer> contacts;
    private BankAccount defaultBankAccount;
    private boolean isDirectMoneyTransferAllowed;
    private String chargeAmount;

    @PostConstruct
    public void init(){
        this.bankAccounts = new ArrayList<>();
        this.contacts = new ArrayList<>();
    }

    public String getCreditInEuro() {
        return creditInEuro;
    }
    public void setCreditInEuro(String creditInEuro) {
        this.creditInEuro = creditInEuro;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }
    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public List<Customer> getContacts() {
        return contacts;
    }
    public void setContacts(List<Customer> contacts) {
        this.contacts = contacts;
    }

    public BankAccount getDefaultBankAccount() {
        return defaultBankAccount;
    }
    public void setDefaultBankAccount(BankAccount defaultBankAccount) {
        this.defaultBankAccount = defaultBankAccount;
    }

    public boolean isDirectMoneyTransferAllowed() {
        return isDirectMoneyTransferAllowed;
    }
    public void setDirectMoneyTransferAllowed(boolean isDirectMoneyTransferAllowed) {
        this.isDirectMoneyTransferAllowed = isDirectMoneyTransferAllowed;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }
    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public CustomerConverter getCustomerConverter() {
        return customerConverter;
    }
    public void setCustomerConverter(CustomerConverter customerConverter) {
        this.customerConverter = customerConverter;
    }

    public BankAccountConverter getBankAccountConverter() {
        return bankAccountConverter;
    }
    public void setBankAccountConverter(BankAccountConverter bankAccountConverter) {
        this.bankAccountConverter = bankAccountConverter;
    }

    @SuppressWarnings("Duplicates")
    public String charge(CustomerModel customerModel){

        String localAmount = this.getChargeAmount();
        int currencySplitIndex=localAmount.length()-3;
        if(localAmount.length() < 3 || localAmount.charAt(currencySplitIndex)!='.' && localAmount.charAt(currencySplitIndex)!=',') localAmount = localAmount + ".00";
        localAmount = localAmount.replace("€", "").replace(".", "").replace(",","");

        for(char c : localAmount.toCharArray()){
            if(c < 48 || c > 57) return "/pages/charge/charge-information-error";
        }
        int amount = Integer.parseInt(localAmount);
        if(amount > 0){
            Transactions transaction = new Transactions();
            transaction.setSender(customerModel.getEmail());
            transaction.setReceiver(customerModel.getEmail());
            transaction.setAmountInEuroCent(amount);
            transaction.setMessage1("Aufladen des PayPal-Guthabens");

            try{
                customerService.chargeMoney(transaction);

                String localCredit = this.getCreditInEuro();
                currencySplitIndex=localCredit.length()-3;
                if(localCredit.length() < 3 || localCredit.charAt(currencySplitIndex)!='.' && localCredit.charAt(currencySplitIndex)!=',') localCredit = localCredit + ".00";
                localCredit = localCredit.replace("€", "").replace(".", "").replace(",","");
                long newAmountInEuroCent = Integer.parseInt(localCredit) + amount;
                String newAmountInEuro = String.valueOf(((double)(newAmountInEuroCent))/100);
                this.setCreditInEuro(newAmountInEuro);
            }
            catch (ITransactionException ex){
                return "/pages/charge/charge-process-failed";
            }
            return "/pages/charge/charge";
        }
        return "/pages/charge/charge-information-error";
    }
}