package com.prechtl.bank.dummy;

public class Transaction {

    public enum TransactionStatus {
        NEW,
        PENDING,
        DONE,
    }

    public enum TransactionType {
        TRANSFER,
        DIRECT_DEBIT
    }

    public enum Duration {
        ONCE,
        DAILY,
        WEEKLY,
        MONTHLY,
        ANNUALLY,DIR
    }

}
