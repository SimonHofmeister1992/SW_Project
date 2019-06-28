package de.hofmeister.service.PaymentService;

import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.entity.transaction.TransactionStatus;
import de.hofmeister.entity.transaction.Transactions;
import de.hofmeister.service.CustomerService.CustomerService;
import de.hofmeister.service.TransactionWorkoffServices.worker.ITransactionWorker;
import de.hofmeister.service.TransactionWorkoffServices.worker.TransactionProcessing;
import de.hofmeister.service.TransactionWorkoffServices.worker.TransactionProcessingType;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;

@RequestScoped
@WebService
public class PaymentService implements HofmeisterPaymentServiceIF {

    @PersistenceContext
    private EntityManager em;

    @Inject
    @TransactionProcessing(value = TransactionProcessingType.NORMAL)
    private ITransactionWorker worker;

    @Inject
    CustomerService customerService;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @WebMethod
    public Transactions commitTransaction (@WebParam(name = "transaction") Transactions transaction ) throws HofmeisterPaymentServiceException {
        if (transaction == null)
            throw new HofmeisterPaymentServiceException(HofmeisterPaymentServiceException.NO_TRANSACTION_AVAILABLE);
        // map from customer to customer-account if necessary

        if (transaction.getReceiver() == null)
            throw new HofmeisterPaymentServiceException(HofmeisterPaymentServiceException.NO_RECEIVER_AVAILABLE);
        CustomerAccount receiver;
        try {
            Customer customer = customerService.getCustomerByEmail(transaction.getReceiver());
            receiver = customerService.getCustomerAccountByCustomer(customer);
        } catch (CustomerService.HofmeisterCustomerServiceException ex) {
            throw new HofmeisterPaymentServiceException(HofmeisterPaymentServiceException.NO_RECEIVER_AVAILABLE);
        }
        if (receiver == null)
            throw new HofmeisterPaymentServiceException(HofmeisterPaymentServiceException.NO_RECEIVER_AVAILABLE);
        transaction.setReceiverAcc(receiver);

        if (transaction.getSender() == null)
            throw new HofmeisterPaymentServiceException(HofmeisterPaymentServiceException.NO_SENDER_AVAILABLE);
        CustomerAccount sender;
        try {
            Customer customer = customerService.getCustomerByEmail(transaction.getSender());
            sender = customerService.getCustomerAccountByCustomer(customer);
        } catch (CustomerService.HofmeisterCustomerServiceException ex) {
            throw new HofmeisterPaymentServiceException(HofmeisterPaymentServiceException.NO_SENDER_AVAILABLE);
        }
        if (sender == null)
            throw new HofmeisterPaymentServiceException(HofmeisterPaymentServiceException.NO_SENDER_AVAILABLE);
        transaction.setSenderAcc(sender);

        // validate
        if (transaction.getAmountInEuroCent() < 0)
            throw new HofmeisterPaymentServiceException(HofmeisterPaymentServiceException.AMOUNT_CANNOT_BE_NEGATIVE);

        // extend by own information

        transaction.setTransactionStatus(TransactionStatus.ASSIGNED);
        transaction.setTransactionDate(new Date());
        transaction.setLastProcessed(new Date());

        em.persist(transaction);
        em.flush();
        worker.workOffTransaction(transaction);
        em.detach(transaction);
        return transaction;
    }

    @Override
    @WebMethod
    public Transactions viewTransactionStatus(@WebParam(name = "transaction") Transactions transaction) throws HofmeisterPaymentServiceException {
        if(transaction == null) throw new HofmeisterPaymentServiceException(HofmeisterPaymentServiceException.NO_TRANSACTION_AVAILABLE);
        else if(transaction.getTransactionId() == null || transaction.getTransactionId() < 0) throw new HofmeisterPaymentServiceException(HofmeisterPaymentServiceException.NO_TRANSACTION_AVAILABLE);
        transaction = em.find(Transactions.class, transaction.getTransactionId());
        em.detach(transaction);
        return transaction;
    }

    @WebMethod(exclude = true)
    public Collection<Transactions> getAllTransactions(){
        TypedQuery<Transactions> query = em.createNamedQuery("Transactions.all", Transactions.class);
        return query.getResultList();
    }

    @WebMethod(exclude = true)
    public Collection<Transactions> getTransactionsOfCustomer(Customer customer){
        CustomerAccount customerAccount=null;
        try {
            customerAccount = customerService.getCustomerAccountByCustomer(customer);
        } catch (CustomerService.HofmeisterCustomerServiceException e) {
            // not reachable for logged in user
        }
        if(customerAccount != null){
            TypedQuery<Transactions> query = em.createQuery("SELECT s FROM Transactions AS s WHERE s.senderAcc=:custParam OR s.receiverAcc=:custParam", Transactions.class);
            query.setParameter("custParam", customerAccount);
            return query.getResultList();
        }
        else return null;
    }

    public class HofmeisterPaymentServiceException extends Exception {
        private String message;

        // for committing a transaction
        public static final String NO_SENDER_AVAILABLE = "There is no registered sender available";
        public static final String NO_RECEIVER_AVAILABLE = "There is no registered receiver available";
        public static final String AMOUNT_CANNOT_BE_NEGATIVE = "The commited amount must not be negative";

        // for viewTransactionStatus
        public static final String NO_TRANSACTION_AVAILABLE = "No transaction available";

        public HofmeisterPaymentServiceException(String message){
            setMessage(message);
        }
        private void setMessage(String message) {
            this.message = message;
        }
    }
}
