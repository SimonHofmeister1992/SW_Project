package de.hofmeister.service.TransactionWorkoffServices.worker;

import javax.faces.bean.ApplicationScoped;
import javax.ws.rs.Produces;
import java.io.Serializable;

@ApplicationScoped
public class TransactionWorkerServiceFactory implements Serializable {

    @Produces
    @TransactionProcessing(TransactionProcessingType.NORMAL)
    public ITransactionWorker getTransactionWorker(){
        return new TransactionWorkerService();
    }

    @Produces
    @TransactionProcessing(TransactionProcessingType.FAST)
    public ITransactionWorker getFastTransactionWorker(){
        return new TransactionFastWorkerService();
    }

}
