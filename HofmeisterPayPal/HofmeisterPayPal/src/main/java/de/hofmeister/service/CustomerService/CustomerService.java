package de.hofmeister.service.CustomerService;

import com.prechtl.bank.*;
import com.prechtl.bank.dummy.*;
import de.hofmeister.entity.bank.BankAccount;
import de.hofmeister.entity.bank.FinanceInstitute;
import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.entity.transaction.TransactionStatus;
import de.hofmeister.entity.transaction.Transactions;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@ApplicationScoped
public class CustomerService implements HofmeisterCustomerServiceIF {

    @PersistenceContext(type= PersistenceContextType.EXTENDED)
    private EntityManager em;

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

    private static final String BIC_OF_PAYPAL_SERVICE = "BANK";
    private static final String IBAN_OF_PAYPAL_SERVICE = "DE20614672326156345227";

    private static final String REASON_OF_CHARGING = "Aufladen des PayPal-Guthabens";

    @Override
    // database-write needed
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @SuppressWarnings("Duplicates")
    public Transactions chargeMoney(Transactions transaction) throws ITransactionException {
        // get bank account of money sender

        Customer sender;
        CustomerAccount senderAcc;

        if(transaction.getSenderAcc() == null){
            String senderEmail = transaction.getSender();
            try {
                sender = this.getCustomerByEmail(senderEmail);
                senderAcc = this.getCustomerAccountByCustomer(sender);
            } catch (HofmeisterCustomerServiceException e) {
                throw new ChargeException(ChargeException.SENDER_COULD_NOT_BE_IDENTIFIED);
            }
        }
        else{
            senderAcc = transaction.getSenderAcc();
        }

        // prepare charge

        if(senderAcc != null){
            BankAccount defaultBankAccount = senderAcc.getDefaultBankAccount();

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setLoginId(defaultBankAccount.getBankLoginId());
            loginDTO.setPassword(defaultBankAccount.getBankLoginPwd());

            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setTransactionType(TransactionType.DIRECT_DEBIT);

            com.prechtl.bank.BankAccount payer = new com.prechtl.bank.BankAccount();
            com.prechtl.bank.BankAccount payee = new com.prechtl.bank.BankAccount();

            BankInstitute payerFI=new BankInstitute(), payeeFI=new BankInstitute();
            payeeFI.setBic(BIC_OF_PAYPAL_SERVICE);
            payerFI.setBic(defaultBankAccount.getFinanceInstitute().getBic());

            payee.setBankInstitute(payeeFI);
            payee.setIban(IBAN_OF_PAYPAL_SERVICE);

            payer.setBankInstitute(payerFI);
            payer.setIban(defaultBankAccount.getIban());

            transactionDTO.setPayee(payee);
            transactionDTO.setPayer(payer);
            transactionDTO.setReasonOfUsage(REASON_OF_CHARGING);
            transactionDTO.setTransactionStatus(com.prechtl.bank.TransactionStatus.NEW);
            transactionDTO.setDuration(Duration.ONCE);
            double amount = transaction.getAmountInEuroCent()/100.0;
            transactionDTO.setAmount(amount);

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
            XMLGregorianCalendar xmlDate;
            try {
                xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            } catch (DatatypeConfigurationException e) {
                throw new ChargeException(ChargeException.MONEY_COULD_NOT_BE_CHARGED);
            }
            transactionDTO.setDate(xmlDate);
            TransactionServiceIF stub;
            try {

                // problems if consumed webservice is not reachable locally, so request-url has to be reset. Time-outs have to be adapted too

                // url: really bad in a running system (i.e. version number changes => this hardcoded string must be adapted)
                String plainUrl = "http://im-lamport.oth-regensburg.de:8080/prechtlbank-0.1/TransactionService?wsdl";
                URL url = new URL(plainUrl);
                TransactionServiceService service = new TransactionServiceService(url);
                stub = service.getTransactionServicePort();
                ((BindingProvider) stub).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "6000");
                ((BindingProvider) stub).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "15000");
                ((BindingProvider)stub).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, plainUrl);


                transactionDTO = stub.directDebit(loginDTO, transactionDTO);



            } catch (TransactionException_Exception e) {
                throw new ChargeException(ChargeException.MONEY_COULD_NOT_BE_CHARGED);
            } catch (LoginException_Exception e) {
                throw new ChargeException(ChargeException.SENDER_COULD_NOT_BE_IDENTIFIED);
            }
            catch (Exception e){
                stub = new TransactionService();
                try {
                    transactionDTO = stub.directDebit(loginDTO, transactionDTO);
                } catch (LoginException_Exception e1) {
                    throw new ChargeException(ChargeException.SENDER_COULD_NOT_BE_IDENTIFIED);
                } catch (TransactionException_Exception e1) {
                    throw new ChargeException(ChargeException.MONEY_COULD_NOT_BE_CHARGED);
                }
            }

            if(transactionDTO.getTransactionStatus().equals(com.prechtl.bank.TransactionStatus.DONE)){
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
    @SuppressWarnings("Duplicates")
    public void addBankAccount(CustomerAccount customerAccount, BankAccount bankAccount) throws HofmeisterCustomerServiceException{
        if(customerAccount == null) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.CUSTOMER_ACCOUNT_NOT_FOUND);
        if(bankAccount == null) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.BANKACCOUNT_NOT_VALID);

        CustomerAccount cA = em.find(CustomerAccount.class, customerAccount.getId());

        boolean isBankAccountPropertyOfCustomer=false;

        for (BankAccount bA : cA.getBankAccounts()) {
            if(bA.getIban().equals(bankAccount.getIban())){
                isBankAccountPropertyOfCustomer=true;
            }
        }
        if(!isBankAccountPropertyOfCustomer) throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.BANKACCOUNT_NOT_VALID);

        TypedQuery<BankAccount> ibanQuery = em.createQuery("SELECT s FROM BankAccount AS s WHERE s.iban=:ibanParam", BankAccount.class);
        ibanQuery.setParameter("ibanParam", bankAccount.getIban());
        BankAccount bAcc = ibanQuery.getSingleResult();
        if(bAcc==null) {
            bAcc = bankAccount;
        }
        bAcc.setBankLoginPwd(bankAccount.getBankLoginPwd());
        bAcc.setBankLoginId(bankAccount.getBankLoginId());
        // iban is new
        try{
            // validate if iban is valid (setter method calls validation)
            bAcc.setIban(bankAccount.getIban());
            FinanceInstitute basicInfoFinanceInstitute = bankAccount.getFinanceInstitute();
            TypedQuery<FinanceInstitute> query = em.createQuery("SELECT s FROM FinanceInstitute s WHERE s.bic=:bicParam AND s.name=:fIName", FinanceInstitute.class);
            query.setParameter("bicParam", basicInfoFinanceInstitute.getBic());
            query.setParameter("fIName", basicInfoFinanceInstitute.getName());
            List<FinanceInstitute> resultList = query.getResultList();
            if(resultList.size() == 0) throw  new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.BANKACCOUNT_NOT_VALID);

            // overwrite finance institute with saved entity with full informations
            bAcc.setFinanceInstitute(resultList.get(0));

            em.persist(bAcc);

            CustomerAccount acc = em.find(CustomerAccount.class, customerAccount.getId());
            if(customerAccount.getDefaultBankAccount().getIban().equals(bankAccount.getIban())){
                acc.setDefaultBankAccount(customerAccount.getDefaultBankAccount());
                acc.setDirectMoneyTransferAllowed(customerAccount.isDirectMoneyTransferAllowed());
                if(acc.isDirectMoneyTransferAllowed() && customerAccount.getDefaultBankAccount().getIban()==bankAccount.getIban()){

                    // proof that it is the customers bank account

                    Transactions transaction = new Transactions();
                    transaction.setSenderAcc(customerAccount);
                    transaction.setReceiverAcc(customerAccount);
                    transaction.setAmountInEuroCent(1);
                    try {
                        chargeMoney(transaction);
                    } catch (ITransactionException e) {
                        throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.BANKACCOUNT_NOT_VALID);
                    }
                }
            }

            acc.addBankAccount(bAcc);
            em.persist(acc);
            em.flush();
        }
        catch(ValidationException ex){
            throw new HofmeisterCustomerServiceException(HofmeisterCustomerServiceException.BANKACCOUNT_NOT_VALID);
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
        return query.getResultList();
    }
    public Collection<BankAccount> getAllBankAccounts(){
        TypedQuery<BankAccount> query = em.createNamedQuery("BankAccount.all", BankAccount.class);
        return query.getResultList();
    }

    public class HofmeisterCustomerServiceException extends Exception {

        private String message;
        public final static String PASSWORD_OR_USER_WRONG = "User or password is wrong";
        public final static String BANKACCOUNT_NOT_VALID = "Bankaccount is not valid";
        public final static String CONTACT_NOT_FOUND = "No user is registered with this email address";
        public final static String CUSTOMER_NOT_FOUND = "The customer does not exist";
        public final static String CUSTOMER_ACCOUNT_NOT_FOUND = "The customer account does not exist";
        public final static String TRANSACTION_NOT_VALID = "The transaction is not valid";

        public HofmeisterCustomerServiceException(String message){
            setMessage(message);
        }

        private void setMessage(String message){
            this.message = message;
        }

    }
}
