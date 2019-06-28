package de.hofmeister.service.TransactionWorkoffServices.worker;

import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.entity.transaction.TransactionStatus;
import de.hofmeister.entity.transaction.Transactions;
import de.hofmeister.service.CustomerService.CustomerService;
import de.hofmeister.service.CustomerService.ITransactionException;
import de.hofmeister.service.TransactionWorkoffServices.mgmt.TransactionManagementService;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;

@Stateless
@TransactionProcessing(TransactionProcessingType.NORMAL)
public class TransactionWorkerService implements ITransactionWorker, Serializable {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private TransactionManagementService tms;

    @Inject
    private CustomerService cs;

    public void workOffTransaction(Transactions transaction){
        tms.putInQueue(transaction);
    }

    @Schedule(
            second = "*/5",
            minute="*",
            hour="*",
            persistent = false
    )
    @SuppressWarnings("Duplicates") // fast and normal worker are identical but fast one doesn't use queue  the of management service
    @Transactional(Transactional.TxType.REQUIRED)
    private void workOffTransaction() {
        // pick one transaction of queue
        Transactions transaction = tms.getFirstFromQueue();
        if (transaction != null) {
            tms.removeFromQueue(transaction);
            // update transaction
            transaction = em.find(Transactions.class, transaction.getId());
            em.detach(transaction);

            if (transaction != null) {
                boolean firstTry = true;

                if (transaction.getTransactionStatus() != TransactionStatus.WITHDRAWED) {
                    if (transaction.getTransactionStatus() == TransactionStatus.CHECKING) firstTry = false;
                    transaction.setTransactionStatus(TransactionStatus.IN_PROGRESS);

                    // check if customers not blocked
                    CustomerAccount sender = transaction.getSenderAcc(), receiver = transaction.getReceiverAcc();
                    while (tms.isLocked(receiver) || tms.isLocked(sender)) {
                        // wait, netter: try to get one lock, instead of waiting for both to be free.
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
                                return;
                            } else {
                                tms.unlockCustomerAccount(transaction.getSenderAcc());
                                tms.unlockCustomerAccount(transaction.getReceiverAcc());
                                tms.putInQueue(transaction);
                                return;
                            }
                        }
                    }

                    sender.setCreditInEuroCent(sender.getCreditInEuroCent() - transaction.getAmountInEuroCent());
                    receiver.setCreditInEuroCent(receiver.getCreditInEuroCent() + transaction.getAmountInEuroCent());
                    transaction.setTransactionStatus(TransactionStatus.FINISHED);
                    persistTransaction(transaction);
                    tms.unlockCustomerAccount(sender);
                    tms.unlockCustomerAccount(receiver);
                } else {
                    tms.removeFromQueue(transaction);
                }
            }
        }
    }

    @Transactional(Transactional.TxType.MANDATORY)
    private void persistTransaction(Transactions t){
        t.setLastProcessed(new Date());
        em.merge(t);
        em.flush();
    }
}
