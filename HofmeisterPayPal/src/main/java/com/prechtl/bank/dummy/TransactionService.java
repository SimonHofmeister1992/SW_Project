package com.prechtl.bank.dummy;

import javax.enterprise.inject.Alternative;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
@Alternative
public class TransactionService implements TransactionServiceIF {

    @Override
    @WebMethod
    public TransactionDTO transfer(@WebParam(name = "loginDTO") LoginDTO loginDTO, @WebParam(name = "transactionDTO") TransactionDTO transactionDTO) throws TransactionException, UserService.LoginException {
        return null;
    }

    @Override
    @WebMethod
    public TransactionDTO directDebit(@WebParam(name = "loginDTO") LoginDTO loginDTO, @WebParam(name = "transactionDTO") TransactionDTO transactionDTO) throws TransactionException, UserService.LoginException {
        // dummy, always says charging is successful;
        transactionDTO.setTransactionStatus(Transaction.TransactionStatus.DONE);
        return transactionDTO;
    }

    public class TransactionException extends Exception {

    }

}
