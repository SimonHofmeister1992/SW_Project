package com.prechtl.bank.dummy;

import de.hofmeister.entity.util.GeneratedIdEntity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

// only dummy for snt-consume, so no persistance

@MappedSuperclass
public class TransactionDTO extends GeneratedIdEntity {
    @Transient
    private String payeeIban;
    @Transient
    private String payerIban;
    @Transient
    private double amount;
    @Transient
    private String payeeBic;
    @Transient
    private String payerBic;
    @Transient
    private String reasonOfUsage;
    @Transient
    private Transaction.TransactionStatus transactionStatus;
    @Transient
    private Transaction.TransactionType transactionType;
    @Transient
    private Transaction.Duration duration;

    public TransactionDTO(){

    }

    public TransactionDTO(String payeeIban, String payerIban, double amount, String payeeBic, String payerBic,
                          Transaction.TransactionStatus transactionStatus, Transaction.TransactionType transactionType, String reasonOfUsage,
                          Transaction.Duration duration) {
        this.payeeIban = payeeIban;
        this.payerIban = payerIban;
        this.amount = amount;
        this.payeeBic = payeeBic;
        this.payerBic = payerBic;
        this.transactionStatus = transactionStatus;
        this.transactionType = transactionType;
        this.reasonOfUsage = reasonOfUsage;
        this.duration = duration;

    }

    public Transaction.TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Transaction.TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Transaction.Duration getDuration() {
        return duration;
    }

    public void setDuration(Transaction.Duration duration) {
        this.duration = duration;
    }

    public String getPayeeBic() {
        return payeeBic;
    }

    public void setPayeeBic(String payeeBic) {
        this.payeeBic = payeeBic;
    }

    public String getPayeeIban() {
        return payeeIban;
    }

    public void setPayeeIban(String payeeIban) {
        this.payeeIban = payeeIban;
    }

    public String getPayerBic() {
        return payerBic;
    }

    public void setPayerBic(String payerBic) {
        this.payerBic = payerBic;
    }

    public String getPayerIban() {
        return payerIban;
    }

    public void setPayerIban(String payerIban) {
        this.payerIban = payerIban;
    }

    public String getReasonOfUsage() {
        return reasonOfUsage;
    }

    public void setReasonOfUsage(String reasonOfUsage) {
        this.reasonOfUsage = reasonOfUsage;
    }

    public Transaction.TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Transaction.TransactionType transactionType) {
        this.transactionType = transactionType;
    }

}

