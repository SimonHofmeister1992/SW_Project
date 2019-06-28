package de.hofmeister.entity.transaction;

import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.entity.util.GeneratedIdEntity;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = "Transactions.all", query = "SELECT s FROM Transactions AS s")
public class Transactions extends GeneratedIdEntity {

    @Transient
    private Long transactionId;
    @Transient
    private String receiverEmail;
    @Transient
    private String senderEmail;
    @XmlTransient
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private CustomerAccount receiverAcc;
    @XmlTransient
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private CustomerAccount senderAcc;
    @NotNull
    private int amountInEuroCent;
    @Temporal(TemporalType.TIMESTAMP)
    @XmlTransient
    private Date lastProcessed;
    @Temporal(TemporalType.TIMESTAMP)
    @XmlTransient
    private Date transactionDate;
    @NotNull
    private TransactionStatus transactionStatus;
    @Size(max = 80)
    private String message1;
    @Size(max = 80)
    private String message2;

    public Transactions() {
    }

    public Transactions(CustomerAccount senderAcc, CustomerAccount receiverAcc, int amountInEuroCent, Date transactionDate) {
        setSenderAcc(senderAcc);
        setReceiverAcc(receiverAcc);
        setAmountInEuroCent(amountInEuroCent);
        setTransactionDate(transactionDate);
        setTransactionStatus(TransactionStatus.ASSIGNED);
    }
    public Transactions(CustomerAccount senderAcc, CustomerAccount receiverAcc, int amountInEuroCent, Date transactionDate, String message1) {
        setSenderAcc(senderAcc);
        setReceiverAcc(receiverAcc);
        setAmountInEuroCent(amountInEuroCent);
        setTransactionDate(transactionDate);
        setTransactionStatus(TransactionStatus.ASSIGNED);
        setMessage1(message1);
    }
    public Transactions(CustomerAccount senderAcc, CustomerAccount receiverAcc, int amountInEuroCent, Date transactionDate, String message1, String message2) {
        setSenderAcc(senderAcc);
        setReceiverAcc(receiverAcc);
        setAmountInEuroCent(amountInEuroCent);
        setTransactionDate(transactionDate);
        setTransactionStatus(TransactionStatus.ASSIGNED);
        setMessage1(message1);
    }

    public CustomerAccount getReceiverAcc() {
        return receiverAcc;
    }
    public void setReceiverAcc(CustomerAccount receiver) {
        this.receiverAcc = receiver;
    }

    public CustomerAccount getSenderAcc() {
        return senderAcc;
    }
    public void setSenderAcc(CustomerAccount sender) {
        this.senderAcc = sender;
    }

    public int getAmountInEuroCent() {
        return amountInEuroCent;
    }
    public void setAmountInEuroCent(int amountInEuroCent) throws ValidationException {
        if (amountInEuroCent < 0) throw new ValidationException("you can't send a negative amount of money");
        this.amountInEuroCent = amountInEuroCent;
    }

    public Date getLastProcessed() {
        return lastProcessed;
    }
    public void setLastProcessed(Date lastProcessed) {
        this.lastProcessed = lastProcessed;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }
    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getMessage1() {
        return message1;
    }
    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    public String getMessage2() {
        return message2;
    }
    public void setMessage2(String message2) {
        this.message2 = message2;
    }

    public String getSender() {
        return senderEmail;
    }
    public void setSender(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiver() {
        return receiverEmail;
    }
    public void setReceiver(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "Transactions{"
                + ", id=" + getId()
                + ", senderEmail=" + getSender()
                + ", receiverEmail=" + getReceiver()
                + ", amountInEuroCent=" + getAmountInEuroCent()
                + ", transactionStatus=" + getTransactionStatus()
                + ", message1=" + getMessage1()
                + ", message2=" + getMessage2()
                + "}";
    }
}

