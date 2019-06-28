
package com.prechtl.bank;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für bankAccount complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="bankAccount"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://service/}generatedIdEntity"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="accountStatus" type="{http://service/}bankAccountStatus" minOccurs="0"/&gt;
 *         &lt;element name="bankInstitute" type="{http://service/}bankInstitute" minOccurs="0"/&gt;
 *         &lt;element name="iban" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="transactions" type="{http://service/}transaction" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="user" type="{http://service/}user" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bankAccount", propOrder = {
    "accountStatus",
    "bankInstitute",
    "iban",
    "transactions",
    "user"
})
public class BankAccount
    extends GeneratedIdEntity
{

    @XmlSchemaType(name = "string")
    protected BankAccountStatus accountStatus;
    protected BankInstitute bankInstitute;
    protected String iban;
    @XmlElement(nillable = true)
    protected List<Transaction> transactions;
    protected User user;

    /**
     * Ruft den Wert der accountStatus-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BankAccountStatus }
     *     
     */
    public BankAccountStatus getAccountStatus() {
        return accountStatus;
    }

    /**
     * Legt den Wert der accountStatus-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BankAccountStatus }
     *     
     */
    public void setAccountStatus(BankAccountStatus value) {
        this.accountStatus = value;
    }

    /**
     * Ruft den Wert der bankInstitute-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BankInstitute }
     *     
     */
    public BankInstitute getBankInstitute() {
        return bankInstitute;
    }

    /**
     * Legt den Wert der bankInstitute-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BankInstitute }
     *     
     */
    public void setBankInstitute(BankInstitute value) {
        this.bankInstitute = value;
    }

    /**
     * Ruft den Wert der iban-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIban() {
        return iban;
    }

    /**
     * Legt den Wert der iban-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIban(String value) {
        this.iban = value;
    }

    /**
     * Gets the value of the transactions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transactions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransactions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Transaction }
     * 
     * 
     */
    public List<Transaction> getTransactions() {
        if (transactions == null) {
            transactions = new ArrayList<Transaction>();
        }
        return this.transactions;
    }

    /**
     * Ruft den Wert der user-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link User }
     *     
     */
    public User getUser() {
        return user;
    }

    /**
     * Legt den Wert der user-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link User }
     *     
     */
    public void setUser(User value) {
        this.user = value;
    }

}
