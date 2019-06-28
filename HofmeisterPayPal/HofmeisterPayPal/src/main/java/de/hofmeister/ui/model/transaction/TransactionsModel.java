package de.hofmeister.ui.model.transaction;

import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.transaction.Transactions;
import de.hofmeister.service.CustomerService.CustomerService;
import de.hofmeister.service.PaymentService.PaymentService;
import de.hofmeister.ui.converter.TransactionsConverter;
import de.hofmeister.ui.model.customer.CustomerModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

@ManagedBean(name = "transactionsModel")
@SessionScoped
public class TransactionsModel {

    @Inject
    private PaymentService paymentService;

    @Inject
    private CustomerService customerService;

    @Inject
    private TransactionsConverter transactionsConverter;

    private Collection<Transactions> transactions;

    public void init(CustomerModel customerModel){
        this.transactions = new ArrayList<>();
        Customer customerSelf = null;
        try {
            customerSelf = customerService.getCustomerByEmail(customerModel.getEmail());
        } catch (CustomerService.HofmeisterCustomerServiceException e) {
            // not reachable for logged in user
        }
        Collection<Transactions> t = null;
        if(customerSelf != null){
            t = paymentService.getTransactionsOfCustomer(customerSelf);
        }
        if(t!=null) this.transactions.addAll(t);
    }

    public Collection<Transactions> getTransactions() {
        return transactions;
    }
    public void setTransactions(Collection<Transactions> transactions) {
        this.transactions = transactions;
    }

    public TransactionsConverter getTransactionsConverter() {
        return transactionsConverter;
    }
    public void setTransactionsConverter(TransactionsConverter transactionsConverter) {
        this.transactionsConverter = transactionsConverter;
    }
}
