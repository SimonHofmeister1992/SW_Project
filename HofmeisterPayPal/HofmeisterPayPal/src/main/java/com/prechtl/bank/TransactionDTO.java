
package com.prechtl.bank;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für transactionDTO complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="transactionDTO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="payee" type="{http://service/}bankAccount" minOccurs="0"/&gt;
 *         &lt;element name="payer" type="{http://service/}bankAccount" minOccurs="0"/&gt;
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="transactionStatus" type="{http://service/}transactionStatus" minOccurs="0"/&gt;
 *         &lt;element name="transactionType" type="{http://service/}transactionType" minOccurs="0"/&gt;
 *         &lt;element name="reasonOfUsage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="duration" type="{http://service/}duration" minOccurs="0"/&gt;
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transactionDTO", propOrder = {
    "payee",
    "payer",
    "amount",
    "transactionStatus",
    "transactionType",
    "reasonOfUsage",
    "duration",
    "date"
})
public class TransactionDTO {

    protected BankAccount payee;
    protected BankAccount payer;
    protected double amount;
    @XmlSchemaType(name = "string")
    protected TransactionStatus transactionStatus;
    @XmlSchemaType(name = "string")
    protected TransactionType transactionType;
    protected String reasonOfUsage;
    @XmlSchemaType(name = "string")
    protected Duration duration;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;

    /**
     * Ruft den Wert der payee-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BankAccount }
     *     
     */
    public BankAccount getPayee() {
        return payee;
    }

    /**
     * Legt den Wert der payee-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BankAccount }
     *     
     */
    public void setPayee(BankAccount value) {
        this.payee = value;
    }

    /**
     * Ruft den Wert der payer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BankAccount }
     *     
     */
    public BankAccount getPayer() {
        return payer;
    }

    /**
     * Legt den Wert der payer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BankAccount }
     *     
     */
    public void setPayer(BankAccount value) {
        this.payer = value;
    }

    /**
     * Ruft den Wert der amount-Eigenschaft ab.
     * 
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Legt den Wert der amount-Eigenschaft fest.
     * 
     */
    public void setAmount(double value) {
        this.amount = value;
    }

    /**
     * Ruft den Wert der transactionStatus-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TransactionStatus }
     *     
     */
    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    /**
     * Legt den Wert der transactionStatus-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionStatus }
     *     
     */
    public void setTransactionStatus(TransactionStatus value) {
        this.transactionStatus = value;
    }

    /**
     * Ruft den Wert der transactionType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TransactionType }
     *     
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * Legt den Wert der transactionType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionType }
     *     
     */
    public void setTransactionType(TransactionType value) {
        this.transactionType = value;
    }

    /**
     * Ruft den Wert der reasonOfUsage-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonOfUsage() {
        return reasonOfUsage;
    }

    /**
     * Legt den Wert der reasonOfUsage-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonOfUsage(String value) {
        this.reasonOfUsage = value;
    }

    /**
     * Ruft den Wert der duration-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Legt den Wert der duration-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setDuration(Duration value) {
        this.duration = value;
    }

    /**
     * Ruft den Wert der date-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Legt den Wert der date-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

}
