package de.hofmeister.ui.converter;

import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.entity.transaction.Transactions;
import de.hofmeister.service.CustomerService.CustomerService;
import de.hofmeister.service.PaymentService.PaymentService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
public class TransactionsConverter implements Converter {

    @Inject
    private PaymentService paymentService;

    @Inject
    private CustomerService customerService;

    private Map<Long, Transactions> transactionsMap;
    private Map<String, Customer> customerMap;

    @PostConstruct
    public void init(){
        if(this.transactionsMap == null){
            Collection<Transactions> transactions = paymentService.getAllTransactions();
            this.transactionsMap = new HashMap<>();
            transactions.forEach(transaction -> {
                transactionsMap.put(transaction.getId(), transaction);
            });
        }
        if(this.customerMap == null){
            this.customerMap = new HashMap<>();
            Collection<Customer> allCustomers = customerService.getAllCustomers();
            allCustomers.forEach(customer -> {
                this.customerMap.put(CustomerAccount.encryptString(customer.getEmailAddress()), customer);
            });
        }
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        String[] split = s.split("-");
        return this.transactionsMap.get(Long.valueOf(split[0].trim()));
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(this.transactionsMap.containsKey(Long.valueOf(o.toString()))){
            Transactions transaction = this.transactionsMap.get(o);
            Customer sender = customerMap.get(transaction.getSenderAcc().getId()), receiver = customerMap.get(transaction.getReceiverAcc().getId());
            double amount = Double.valueOf(transaction.getAmountInEuroCent())/100;
            String amountAsString = String.valueOf(amount);
            if(amountAsString.charAt(amountAsString.length()-2)=='.') amountAsString = amountAsString + "0";
            return "TransaktionsID: " + transaction.getId() + "  -  Datum: " + transaction.getTransactionDate() + "  -  Sender: " + sender.getFirstName() + " " + sender.getSecondName()
                    + "  -  Empfänger: " + receiver.getFirstName() + " "  + receiver.getSecondName() + "  -  "
                    + "Betrag: " + amountAsString + " €" + "  -  Status: " + transaction.getTransactionStatus();
        }
            throw new IllegalArgumentException("Object must be the Transaction-ID but is " + o.toString());
    }

    public void addTransaction(Transactions t){
        this.transactionsMap.put(t.getId(), t);
    }
}
