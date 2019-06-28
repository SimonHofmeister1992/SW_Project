package de.hofmeister.service.TransactionWorkoffServices.mgmt;

import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.entity.transaction.Transactions;

import javax.ejb.Singleton;
import javax.transaction.Transactional;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.HashMap;

@Singleton
public class TransactionManagementService {

    // manages all locks of customers for transactions and the transactions - in the whole application

    private HashMap<CustomerAccount, Date> blockedAccounts;
    private ArrayDeque<Transactions> transactions;

    public TransactionManagementService(){
        transactions = new ArrayDeque<>();
        blockedAccounts = new HashMap<>();
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void putInQueue(Transactions transaction){
        if(transaction != null)
        transactions.addLast(transaction);
    }
    public Transactions getFirstFromQueue(){
        if(transactions.size() == 0) return null;
        return transactions.getFirst();
    }
    public void removeFromQueue(Transactions transaction){
        if(transaction != null)
        transactions.remove(transaction);
    }

    public void lockCustomerAccount(CustomerAccount ca){
        blockedAccounts.put(ca, new Date());
    }
    public void unlockCustomerAccount(CustomerAccount ca){
        blockedAccounts.remove(ca);
    }
    public boolean isLocked(CustomerAccount customerAccount){
        if(blockedAccounts.containsKey(customerAccount)) return true;
        return false;
    }
}
