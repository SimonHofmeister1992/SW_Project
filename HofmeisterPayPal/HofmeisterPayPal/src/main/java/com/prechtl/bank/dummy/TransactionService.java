package com.prechtl.bank.dummy;

import com.prechtl.bank.*;
import com.prechtl.bank.LoginDTO;
import com.prechtl.bank.TransactionDTO;

import javax.jws.WebService;

@WebService
public class TransactionService implements TransactionServiceIF {


    @Override
    public TransactionDTO directDebit(LoginDTO arg0, TransactionDTO arg1) throws LoginException_Exception, TransactionException_Exception {
        // in real system: throw error - bank not reachable
        // here for demonstration purposes that charging would work: charging successful
        arg1.setTransactionStatus(TransactionStatus.DONE);
        System.out.println("HofmeisterPayPal: Bank-Consume-Service: Service could not be reached. Dummy: Charge Successful");
        return arg1;
    }

    @Override
    public TransactionDTO transfer(LoginDTO arg0, TransactionDTO arg1) throws LoginException_Exception, TransactionException_Exception {
        return null;
    }

    public class TransactionException extends Exception {

    }

}
