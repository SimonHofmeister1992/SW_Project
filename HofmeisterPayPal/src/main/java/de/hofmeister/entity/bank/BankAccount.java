package de.hofmeister.entity.bank;

import de.hofmeister.entity.util.GeneratedIdEntity;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NamedQuery(name = "BankAccount.all", query = "SELECT s FROM BankAccount AS s")
public class BankAccount extends GeneratedIdEntity {

    private final static int MIN_IBAN_LENGTH = 15;
    private final static int MAX_IBAN_LENGTH = 32;

    @NotNull
    @Size(min = MIN_IBAN_LENGTH, max = MAX_IBAN_LENGTH)
    @Column(unique = true)
    private String iban;
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private FinanceInstitute financeInstitute;
    private String bankLoginId;
    private String bankLoginPwd;

    public BankAccount() {

    }
    public BankAccount(String iban, FinanceInstitute fi) throws ValidationException {
        this.setIban(iban);
        this.setFinanceInstitute(fi);
    }

    public String getIban() {
        return this.iban;
    }
    public void setIban(String iban) throws ValidationException {
        iban.replaceAll(" ", "");
        iban.trim();
        if (validateIBAN(iban)) {
            this.iban = iban;
        } else throw new ValidationException("IBAN is not valid");
    }

    public FinanceInstitute getFinanceInstitute() {
        return financeInstitute;
    }
    public void setFinanceInstitute(FinanceInstitute financeInstitute) {
        this.financeInstitute = financeInstitute;
    }

    public String getBankLoginId() {
        return bankLoginId;
    }
    public void setBankLoginId(String bankLoginId) {
        this.bankLoginId = bankLoginId;
    }

    public String getBankLoginPwd() {
        return bankLoginPwd;
    }
    public void setBankLoginPwd(String bankLoginPwd) {
        this.bankLoginPwd = bankLoginPwd;
    }

    protected boolean validateIBAN(String iban) {
        boolean isIBANValid = false;
        if (iban.length() >= MIN_IBAN_LENGTH && iban.length() <= MAX_IBAN_LENGTH) {
            if (iban.substring(0, 2).matches("^[A-Z]{2}$")) {
                //Prüfziffer-Validierung vorbereiten
                int ibanRest = calculateIBANModulo(iban);
                // Berechnete Prüfziffern mit dem Original vergleichen
                if (ibanRest == 1) {
                    isIBANValid = true;
                }
            }
        }
        return isIBANValid;
    }
    protected int calculateIBANModulo(String iban) {
        String validationDigits = iban.substring(2, 4);
        iban = iban.substring(4) + iban.substring(0, 2) + validationDigits;
        iban = iban.toUpperCase();

        StringBuffer newIBAN = new StringBuffer();
        for (char character : iban.toCharArray()) {
            if (character >= 'A' && character <= 'Z') {
                newIBAN.append(Character.getNumericValue(character));
            } else {
                newIBAN.append(character);
            }
        }

        String preprocessedIBAN = newIBAN.toString();
        String actualPart;
        int actualRest = 0;
        Long actualValue = 0L;
        while (preprocessedIBAN.length() >= 3 || Integer.parseInt(preprocessedIBAN) > 97) {
            if (preprocessedIBAN.length() >= 9) actualPart = preprocessedIBAN.substring(0, 9);
            else actualPart = preprocessedIBAN;
            actualValue = Long.valueOf(actualPart) % 97;
            actualRest = Integer.parseInt(Long.toString(actualValue));
            preprocessedIBAN = preprocessedIBAN.replaceFirst(actualPart, String.valueOf(actualRest));
            preprocessedIBAN.replace("^0+", "");
        }
        return actualRest;
    }

}
