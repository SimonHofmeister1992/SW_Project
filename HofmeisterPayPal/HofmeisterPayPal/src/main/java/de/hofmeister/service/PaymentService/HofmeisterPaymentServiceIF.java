package de.hofmeister.service.PaymentService;

import de.hofmeister.entity.transaction.Transactions;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName="HofmeisterPaymentServiceIF")
public interface HofmeisterPaymentServiceIF {
    @WebMethod
    Transactions commitTransaction(@WebParam(name = "transaction") Transactions transaction) throws PaymentService.HofmeisterPaymentServiceException;
    @WebMethod
    Transactions viewTransactionStatus(@WebParam(name = "transaction") Transactions transaction) throws PaymentService.HofmeisterPaymentServiceException;
}
