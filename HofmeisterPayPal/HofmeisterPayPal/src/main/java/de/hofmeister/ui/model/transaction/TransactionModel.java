package de.hofmeister.ui.model.transaction;

import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.transaction.Transactions;
import de.hofmeister.service.PaymentService.PaymentService;
import de.hofmeister.ui.converter.TransactionsConverter;
import de.hofmeister.ui.model.customer.CustomerModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.Collection;

@RequestScoped
@ManagedBean(name = "transactionModel")
public class TransactionModel {

    @Inject
    private PaymentService paymentService;

    private Customer contact;
    private String emailReceiver;
    private String amount;
    private String message1;
    private String message2;

    public String getEmailReceiver() {
        return emailReceiver;
    }
    public void setEmailReceiver(String emailReceiver) {
        this.emailReceiver = emailReceiver;
    }

    public String getMessage1() {
        return message1;
    }
    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    public String getMessage2() {
        return message2;
    }
    public void setMessage2(String message2) {
        this.message2 = message2;
    }

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Customer getContact() {
        return contact;
    }
    public void setContact(Customer contact) {
        this.contact = contact;
    }

    @SuppressWarnings("Duplicates")
    public String submitTransaction(CustomerModel customerModel, TransactionsModel transactionsModel) {
        if (getEmailReceiver() == null) {
            if (this.getContact() == null) return "/pages/transaction/transactionError";
            else {
                this.setEmailReceiver(this.getContact().getEmailAddress());
            }
        }
        String localAmount = this.getAmount();
        int currencySplitIndex = localAmount.length() - 3;
        if (localAmount.length() < 3 || localAmount.charAt(currencySplitIndex) != '.' && localAmount.charAt(currencySplitIndex) != ',')
            localAmount = localAmount + ".00";
        localAmount = localAmount.replace("€", "").replace(".", "").replace(",", "");
        for (char c : localAmount.toCharArray()) {
            if (c < 48 || c > 57) return "/pages/transaction/transaction-error";
        }
        if (Integer.parseInt(localAmount) < 0) return "/pages/transaction/transaction-error";

        Transactions transaction = new Transactions();
        transaction.setSender(customerModel.getEmail());
        transaction.setReceiver(this.getEmailReceiver());

        transaction.setAmountInEuroCent(Integer.parseInt(localAmount));
        transaction.setMessage1(this.getMessage1());
        transaction.setMessage2(this.getMessage2());

        try {
            paymentService.commitTransaction(transaction);
        } catch (PaymentService.HofmeisterPaymentServiceException ex) {
            return "/pages/transaction/transaction-error";
        }
        TransactionsConverter converter = transactionsModel.getTransactionsConverter();

        converter.addTransaction(transaction);
        Collection<Transactions> transactions = transactionsModel.getTransactions();
        transactions.add(transaction);
        transactionsModel.setTransactions(transactions);
        return "/pages/transaction/transactions";
    }
}
