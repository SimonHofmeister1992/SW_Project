package de.hofmeister.entity.customer;

import de.hofmeister.entity.bank.BankAccount;
import de.hofmeister.entity.util.StringIdEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class CustomerAccount extends StringIdEntity {

    @NotNull
    private String password;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstActivationDate;
    @NotNull
    private boolean isActive;
    @NotNull
    private int creditInEuroCent;
    @OneToOne
    private BankAccount defaultBankAccount;
    @NotNull
    private Boolean isDirectMoneyTransferAllowed;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<BankAccount> bankAccounts;
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    // Two Lists of FetchType.EAGER aren't allowed
    // solution from https://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags
    private List<Customer> contacts;

    public CustomerAccount() {
        this.bankAccounts = new ArrayList<>();
        this.contacts = new ArrayList<>();
        setDirectMoneyTransferAllowed(Boolean.FALSE);
    }
    public CustomerAccount(String emailAddress, String password) {
        this.contacts = new ArrayList<>();
        this.bankAccounts = new ArrayList<>();
        setFirstActivationDate(new Date());
        setPassword(password);
        setActive(Boolean.TRUE);
        setCreditInEuroCent(0);
        setDirectMoneyTransferAllowed(Boolean.FALSE);
        setId(emailAddress);
    }

    protected void setId(String emailAddress) {
        this.id = encryptString(emailAddress);
    }

    public Date getFirstActivationDate() {
        return firstActivationDate;
    }
    public void setFirstActivationDate(Date firstActivationDate) {
        this.firstActivationDate = firstActivationDate;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }

    public BankAccount getDefaultBankAccount() {
        return defaultBankAccount;
    }
    public void setDefaultBankAccount(BankAccount defaultBankAccount) {
        this.defaultBankAccount = defaultBankAccount;
    }

    public Boolean isDirectMoneyTransferAllowed() {
        return isDirectMoneyTransferAllowed;
    }
    public void setDirectMoneyTransferAllowed(Boolean directMoneyTransferAllowed) {
        isDirectMoneyTransferAllowed = directMoneyTransferAllowed;
    }

    public int getCreditInEuroCent() throws ValidationException {
        return creditInEuroCent;
    }
    public void setCreditInEuroCent(int creditInEuroCent) {
        if (creditInEuroCent < 0) throw new ValidationException("Can't have negative credit");
        else this.creditInEuroCent = creditInEuroCent;
    }

    private void setPassword(String password) {
            this.password = encryptString(password);
    }
    public String getPassword() {
        return password;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }
    public void addBankAccount(BankAccount bankAccount) {
        if (!this.bankAccounts.contains(bankAccount)) this.bankAccounts.add(bankAccount);
    }
    public void removeBankAccount(BankAccount bankAccount) {
        this.bankAccounts.remove(bankAccount);
    }

    public List<Customer> getContacts() {
        return contacts;
    }
    public void addContact(Customer customer) {
        if (!this.contacts.contains(customer))
            this.contacts.add(customer);
    }
    public void removeContact(CustomerAccount customer) {
        this.contacts.remove(customer);
    }

    public static String encryptString(String string){

        // encryption mechanism adapted from: https://stackoverflow.com/questions/33085493/how-to-hash-a-password-with-sha-512-in-java
        // also nearly equals EntityUtils hash-algorithm

        String encryptedString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(string.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            encryptedString = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedString;
    }
}
