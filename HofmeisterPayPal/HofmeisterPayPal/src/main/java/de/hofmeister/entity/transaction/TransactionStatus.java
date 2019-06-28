package de.hofmeister.entity.transaction;

public enum TransactionStatus {
    //ASSIGNED: transaction is in the system
    //CHECKING: check if requirements are met (short period of time)
    //IN_PROGRESS: transaction work off is in progress (short period of time)
    //FINISHED: transaction successful
    //ABORTED: requirements weren't met or system error
    //WITHDRAWED: the user revoked the transaction

    ASSIGNED(0), CHECKING(1), IN_PROGRESS(2), FINISHED(3), ABORTED(4), WITHDRAWED(5);

    private int numVal;

    TransactionStatus(int numVal){
        this.numVal = numVal;
    }

    public int getNumVal(){
        return numVal;
    }
}
