package de.hofmeister.service.CustomerService;

import com.prechtl.bank.dummy.*;
import de.hofmeister.entity.bank.BankAccount;
import de.hofmeister.entity.bank.FinanceInstitute;
import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.entity.transaction.TransactionStatus;
import de.hofmeister.entity.transaction.Transactions;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class CustomerService implements HofmeisterCustomerServiceIF {

    @PersistenceContext
    private EntityManager em;

    @Inject
    TransactionServiceIF transactionServiceIF;

    @Override
    // database read-only, so no transactional context needed
    public CustomerAccount login(Customer customer) throws HofmeisterCustomerServiceException{
        if(customer == null) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.CUSTOMER_NOT_FOUND);
        if(customer.getEmailAddress() == null || customer.getEmailAddress().isEmpty() || customer.getPassword() == null || customer.getPassword().isEmpty()) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.PASSWORD_OR_USER_WRONG);

        TypedQuery<CustomerAccount> query = em.createQuery("SELECT s FROM CustomerAccount s WHERE s.id=:hashedEmail", CustomerAccount.class);
        query.setParameter("hashedEmail", CustomerAccount.encryptString(customer.getEmailAddress()));
        List<CustomerAccount> resultList = query.getResultList();

        if(resultList.size() == 0) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.PASSWORD_OR_USER_WRONG);
        else{
            CustomerAccount ca = resultList.get(0);
            if(ca.getPassword().equals(CustomerAccount.encryptString(customer.getPassword()))){
                return ca;
            }
            else {
                throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.PASSWORD_OR_USER_WRONG);
            }
        }
    }

    private static final String BIC_OF_PAYPAL_SERVICE = "75050000";
    private static final String IBAN_OF_PAYPAL_SERVICE = "DE20614672326156345227";
    private static final String REASON_OF_CHARGING = "Aufladen des PayPal-Guthabens";

    @Override
    // database-write needed
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Transactions chargeMoney(Transactions transaction) throws ITransactionException {
        // get bank account of money sender

        String senderEmail = transaction.getSender();
        Customer sender = null;
        CustomerAccount senderAcc = null;
        try {
            sender = this.getCustomerByEmail(senderEmail);
            senderAcc = this.getCustomerAccountByCustomer(sender);
        } catch (HofmeisterCustomerServiceException e) {
            throw new ChargeException(ChargeException.SENDER_COULD_NOT_BE_IDENTIFIED);
        }

        if(senderAcc != null){
            BankAccount defaultBankAccount = senderAcc.getDefaultBankAccount();

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setLoginId(defaultBankAccount.getBankLoginId());
            loginDTO.setPassword(defaultBankAccount.getBankLoginPwd());

            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setTransactionType(Transaction.TransactionType.DIRECT_DEBIT);
            transactionDTO.setPayeeIban(IBAN_OF_PAYPAL_SERVICE);
            transactionDTO.setPayerIban(defaultBankAccount.getIban());
            transactionDTO.setReasonOfUsage(REASON_OF_CHARGING);
            transactionDTO.setPayeeBic(BIC_OF_PAYPAL_SERVICE);
            transactionDTO.setPayerBic(defaultBankAccount.getFinanceInstitute().getBic());
            transactionDTO.setDuration(Transaction.Duration.ONCE);
            transactionDTO.setAmount(transaction.getAmountInEuroCent());

            try {
                transactionDTO = transactionServiceIF.directDebit(loginDTO, transactionDTO);
            } catch (TransactionService.TransactionException e) {
                throw new ChargeException(ChargeException.MONEY_COULD_NOT_BE_CHARGED);
            } catch (UserService.LoginException e) {
                throw new ChargeException(ChargeException.SENDER_COULD_NOT_BE_IDENTIFIED);
            }
            if(transactionDTO.getTransactionStatus().equals(Transaction.TransactionStatus.DONE)){
                int newCredit = senderAcc.getCreditInEuroCent() + ((int)(transactionDTO.getAmount())); // first is credit in cent, getAmount is a double which deals with Euros
                senderAcc.setCreditInEuroCent(newCredit);
                transaction.setTransactionStatus(TransactionStatus.FINISHED);
                transaction.setSenderAcc(senderAcc);
                transaction.setReceiverAcc(senderAcc);
                transaction.setMessage1(REASON_OF_CHARGING);
                transaction.setTransactionDate(new Date());
                em.persist(transaction);
                em.merge(senderAcc);
                em.flush();
                return transaction;
            }
            else{
                throw new ChargeException(ChargeException.MONEY_COULD_NOT_BE_CHARGED);
            }
        }
        throw new ChargeException(ChargeException.SENDER_COULD_NOT_BE_IDENTIFIED);
    }

    @Override
    // database-write needed
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void addBankAccount(CustomerAccount customerAccount, BankAccount bankAccount) throws HofmeisterCustomerServiceException{
        if(customerAccount == null) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.CUSTOMER_ACCOUNT_NOT_FOUND);
        if(bankAccount == null) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.BANKACCOUNT_NOT_VALID);

        TypedQuery<BankAccount> ibanQuery = em.createQuery("SELECT s FROM BankAccount AS s WHERE s.iban=:ibanParam", BankAccount.class);
        ibanQuery.setParameter("ibanParam", bankAccount.getIban());
        List<BankAccount> ibanResults = ibanQuery.getResultList();

        // iban already exists
        if(ibanResults.size() > 0) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.BANKACCOUNT_NOT_VALID);

        // iban is new
        try{
            // validate if iban is valid (setter method calls validation)
            bankAccount.setIban(bankAccount.getIban());
            FinanceInstitute basicInfoFinanceInstitute = bankAccount.getFinanceInstitute();
            TypedQuery<FinanceInstitute> query = em.createQuery("SELECT s FROM FinanceInstitute s WHERE s.bic=:bicParam AND s.name=:fIName", FinanceInstitute.class);
            query.setParameter("bicParam", basicInfoFinanceInstitute.getBic());
            query.setParameter("fIName", basicInfoFinanceInstitute.getName());
            List<FinanceInstitute> resultList = query.getResultList();
            if(resultList.size() == 0) throw  new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.BANKACCOUNT_NOT_VALID);

            // overwrite finance institute with saved entity with full informations
            bankAccount.setFinanceInstitute(resultList.get(0));
            em.persist(bankAccount);
            CustomerAccount acc = em.find(CustomerAccount.class, customerAccount.getId());
            if(customerAccount.getDefaultBankAccount().getIban().equals(bankAccount.getIban())){
                acc.setDefaultBankAccount(customerAccount.getDefaultBankAccount());
                acc.setDirectMoneyTransferAllowed(customerAccount.isDirectMoneyTransferAllowed());
                if(acc.isDirectMoneyTransferAllowed()){

                    // proof that it is the customers bank account

                    LoginDTO loginDTO = new LoginDTO();
                    loginDTO.setLoginId(bankAccount.getBankLoginId());
                    loginDTO.setPassword(bankAccount.getBankLoginPwd());

                    TransactionDTO transactionDTO = new TransactionDTO();
                    transactionDTO.setAmount(0.01);
                    transactionDTO.setDuration(Transaction.Duration.ONCE);
                    transactionDTO.setPayeeBic(BIC_OF_PAYPAL_SERVICE);
                    transactionDTO.setPayerBic(bankAccount.getFinanceInstitute().getBic());
                    transactionDTO.setPayeeIban(IBAN_OF_PAYPAL_SERVICE);
                    transactionDTO.setPayerIban(bankAccount.getIban());
                    transactionDTO.setReasonOfUsage(REASON_OF_CHARGING);
                    transactionDTO.setTransactionType(Transaction.TransactionType.DIRECT_DEBIT);

                    transactionServiceIF.directDebit(loginDTO, transactionDTO);
                }
            }

            acc.addBankAccount(bankAccount);
            em.persist(acc);
            em.flush();
        }
        catch(ValidationException ex){
            throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.BANKACCOUNT_NOT_VALID);
        } catch (TransactionService.TransactionException e) {
            throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.BANKACCOUNT_NOT_VALID);
        } catch (UserService.LoginException e) {
            throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.PASSWORD_OR_USER_WRONG);
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    // database-write needed
    public void addContact(CustomerAccount customerAccount, Customer contact) throws HofmeisterCustomerServiceException{
       if(customerAccount == null) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.CUSTOMER_ACCOUNT_NOT_FOUND);
       if(contact == null) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.CUSTOMER_NOT_FOUND);
            CustomerAccount acc = em.find(CustomerAccount.class, customerAccount.getId());
            acc.addContact(contact);
            em.persist(acc);
            em.flush();
    }

    public CustomerAccount getCustomerAccountByCustomer(Customer customer) throws HofmeisterCustomerServiceException {
        if(customer == null) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.CUSTOMER_NOT_FOUND);
        CustomerAccount customerAccount = em.find(CustomerAccount.class, CustomerAccount.encryptString(customer.getEmailAddress()));
        if(customerAccount == null) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.CUSTOMER_ACCOUNT_NOT_FOUND);
        em.detach(customerAccount);
        return customerAccount;
    }
    public Customer getCustomerByEmail(String customerEmail) throws HofmeisterCustomerServiceException {
        Customer customer;

        TypedQuery<Customer> query = em.createQuery("SELECT s FROM Customer s WHERE s.emailAddress=:emailParam", Customer.class);
        query.setParameter("emailParam", customerEmail);

        List<Customer> resultList = query.getResultList();

        if(resultList.size() == 0)  throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.CUSTOMER_NOT_FOUND);
        else customer = resultList.get(0);
        em.detach(customer);
        return customer;
    }
    public Collection<Customer> getAllCustomers(){
        TypedQuery<Customer> query = em.createNamedQuery("Customer.all", Customer.class);
        List<Customer> resultList = query.getResultList();
        return resultList;
    }
    public Collection<BankAccount> getAllBankAccounts(){
        TypedQuery<BankAccount> query = em.createNamedQuery("BankAccount.all", BankAccount.class);
        List<BankAccount> resultList = query.getResultList();
        return resultList;
    }

    public class HofmeisterCustomerServiceException extends Exception {

        private String message;
        public final static String PASSWORD_OR_USER_WRONG = "User or password is wrong";
        public final static String BANKACCOUNT_NOT_VALID = "Bankaccount is not valid";
        public final static String CONTACT_NOT_FOUND = "No user is registered with this email address";
        public final static String CUSTOMER_NOT_FOUND = "The customer does not exist";
        public final static String CUSTOMER_ACCOUNT_NOT_FOUND = "The customer account does not exist";

        public HofmeisterCustomerServiceException(String message){
            setMessage(message);
        }

        private void setMessage(String message){
            this.message = message;
        }

    }
}
