package de.hofmeister.service.TransactionWorkoffServices.worker;

import de.hofmeister.entity.transaction.Transactions;

public interface ITransactionWorker {
    void workOffTransaction(Transactions transaction);
}
