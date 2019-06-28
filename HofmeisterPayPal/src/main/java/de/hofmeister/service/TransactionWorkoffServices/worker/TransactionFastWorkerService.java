package de.hofmeister.service.TransactionWorkoffServices.worker;

import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.entity.transaction.TransactionStatus;
import de.hofmeister.entity.transaction.Transactions;
import de.hofmeister.service.CustomerService.CustomerService;
import de.hofmeister.service.CustomerService.ITransactionException;
import de.hofmeister.service.TransactionWorkoffServices.mgmt.TransactionManagementService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;

/* doesn't have to wait in queue */

@Stateless
@TransactionProcessing(TransactionProcessingType.FAST)
public class TransactionFastWorkerService implements ITransactionWorker, Serializable {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private CustomerService cs;

    @Inject
    private TransactionManagementService tms;

    @Transactional(Transactional.TxType.REQUIRED)
    public void workOffTransaction(Transactions transaction){
        em.detach(transaction);
        if (transaction != null) {
            boolean success = workOffTransaction(true, transaction);
            if (!success) {
                workOffTransaction(false, transaction);
            }
        }
    }

    @SuppressWarnings("Duplicates")  // fast and normal worker are identical but fast one doesn't use queue  the of management service
    @Transactional(Transactional.TxType.MANDATORY)
    private boolean workOffTransaction(boolean firstTry, Transactions transaction) {

            transaction.setTransactionStatus(TransactionStatus.IN_PROGRESS);
            CustomerAccount sender = transaction.getSenderAcc(), receiver = transaction.getReceiverAcc();
            while (tms.isLocked(receiver) || tms.isLocked(sender)) {
                // wait, better: try to get one lock, instead of waiting for both to be free.
                // also it should be possible to remove locks in real program => Coffman-Conditions
            }

            tms.lockCustomerAccount(receiver);
            tms.lockCustomerAccount(sender);

            transaction.setTransactionStatus(TransactionStatus.CHECKING);
            if (transaction.getAmountInEuroCent() > transaction.getSenderAcc().getCreditInEuroCent()) {
                try {
                    cs.chargeMoney(transaction);
                } catch (ITransactionException ex) {
                    if (!firstTry) {
                        tms.unlockCustomerAccount(transaction.getSenderAcc());
                        tms.unlockCustomerAccount(transaction.getReceiverAcc());
                        transaction.setTransactionStatus(TransactionStatus.ABORTED);
                        persistTransaction(transaction);
                        return false;
                    } else {
                        tms.unlockCustomerAccount(transaction.getSenderAcc());
                        tms.unlockCustomerAccount(transaction.getReceiverAcc());
                        return false;
                    }
                }
            }

            sender.setCreditInEuroCent(sender.getCreditInEuroCent() - transaction.getAmountInEuroCent());
            receiver.setCreditInEuroCent(receiver.getCreditInEuroCent() + transaction.getAmountInEuroCent());
            transaction.setTransactionStatus(TransactionStatus.FINISHED);
            persistTransaction(transaction);
            tms.unlockCustomerAccount(sender);
            tms.unlockCustomerAccount(receiver);
            return true;
    }

    @Transactional(Transactional.TxType.MANDATORY)
    private void persistTransaction(Transactions t){
        t.setLastProcessed(new Date());
        em.merge(t);
        em.flush();
    }
}
