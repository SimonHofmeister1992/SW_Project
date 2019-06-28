package de.hofmeister.service.InitService;

import de.hofmeister.entity.address.Address;
import de.hofmeister.entity.address.City;
import de.hofmeister.entity.address.Country;
import de.hofmeister.entity.bank.BankAccount;
import de.hofmeister.entity.bank.FinanceInstitute;
import de.hofmeister.entity.customer.Customer;
import de.hofmeister.entity.customer.CustomerAccount;
import de.hofmeister.entity.customer.CustomerType;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class InitService {

    @PersistenceContext
    private EntityManager em;

    private static boolean isInit = false;

    // define values which will be reused in each init-process and for checking if the datasets are already in.

    Country country = new Country("DE", "Deutschland");
    City city = new City("93049", country, "Regensburg");
    String personalStreet = "my-secret-street";
    String personalHouseNumber="666";
    String businessStreetSteamon="steamon-street";
    String businessHouseNumberSteamon="44";
    String businessStreetMarketPlace="marketplace-street";
    String businessHouseNumberMarketPlace="33";
    String businessStreetFinanceInstitute="Lilienthalstraße";
    String businessHouseNumberFinanceInstitute="5";
    Address personalAddress;
    Address businessAddressSteamon;
    Address businessAddressMarketPlace;
    Address businessAddressFinanceInstitute;

    FinanceInstitute financeInstituteBank;
    String bicFIBank = "BANK";
    String nameFIBank = "BANK";

    FinanceInstitute financeInstituteIngolstadt;
    String bicFIIngolstadt = "INGE1SZ6";
    String nameFIIngolstadt = "Ingolstadt PB";

    FinanceInstitute financeInstituteAmberg;
    String bicFIAmberg = "AMGE1SZ6";
    String nameFIAmberg = "Amberg PB";

    FinanceInstitute financeInstituteRatisbona;
    String bicFIRatisbona = "RPGE1SZ6";
    String nameFIRatisbona = "Regensburg PB";

    BankAccount bankAccountSteamon;
    BankAccount bankAccountMarketPlace;
    BankAccount bankAccountPersonal;
    String ibanSteamon = "DE02100100100006820101";
    String ibanMarketPlace ="DE27100777770209299700";
    String ibanPersonal="DE62480599303994628351";

    String bankAccPwdPersonal="abc";
    String bankAccLoginPersonal="JME261";

    Customer customerPersonal;
    Customer customerSteamon;
    Customer customerMarketPlace;

    Date customerPersonalBirthdate = new Date();
    String customerPersonalEmailAddress = "josef@meier.de";
    String customerPersonalFirstName = "Josef";
    String customerPersonalSecondName = "Meier";

    String customerSteamonEmailAddress = "payment@steamon.com";
    String customerSteamonFirstName = "Payment";
    String customerSteamonSecondName = "Steamon";

    String customerMarketplaceEmailAddress = "payment@marketplace.com";
    String customerMarketplaceFirstName = "Payment";
    String customerMarketplaceSecondName = "Marketplace";

    CustomerAccount customerAccountPersonal;
    CustomerAccount customerAccountMarketplace;
    CustomerAccount customerAccountSteamon;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void init() {
        if(!isInit){
            initAddress();
            initFinanceInstitute();
            initBankAccount();
            initCustomer();
            initCustomerAccount();
            isInit = true;
        }
    }

    private void initAddress() {
        personalAddress = initAddressValues(personalStreet, personalHouseNumber, city);
        businessAddressMarketPlace = initAddressValues(businessStreetMarketPlace, businessHouseNumberMarketPlace, city);
        businessAddressSteamon = initAddressValues(businessStreetSteamon, businessHouseNumberSteamon, city);
        businessAddressFinanceInstitute = initAddressValues(businessStreetFinanceInstitute, businessHouseNumberFinanceInstitute, city);
        em.flush();
    }
    private Address initAddressValues(String street, String houseNumber, City city){
        // search for init-address. needed after program-restart
        TypedQuery<Address> query = em.createQuery("SELECT s FROM Address s WHERE s.city=:city AND s.street=:street AND s.houseNumber=:houseNumber", Address.class);
        query.setParameter("city", city);
        query.setParameter("street", street);
        query.setParameter("houseNumber", houseNumber);
        List<Address> resultList = query.getResultList();
        if(resultList.size() == 0){
            Address address = new Address();
            address.setStreet(street);
            address.setHouseNumber(houseNumber);
            address.setCity(city);
            em.persist(address);
            return address;
        }
        return resultList.get(0);
    }
    private void initFinanceInstitute(){
        // search for init-address. needed after program-restart
        TypedQuery<FinanceInstitute> query = em.createQuery("SELECT s FROM FinanceInstitute s WHERE s.name=:nameFI AND s.bic=:bicFI", FinanceInstitute.class);
        query.setParameter("nameFI", nameFIBank);
        query.setParameter("bicFI", bicFIBank);
        List<FinanceInstitute> resultList = query.getResultList();

        if(resultList.size() == 0) {
            initFIValues(financeInstituteBank, businessAddressFinanceInstitute, nameFIBank, bicFIBank);
            initFIValues(financeInstituteIngolstadt, businessAddressFinanceInstitute, nameFIIngolstadt, bicFIIngolstadt);
            initFIValues(financeInstituteAmberg, businessAddressFinanceInstitute, nameFIAmberg, bicFIAmberg);
            initFIValues(financeInstituteRatisbona, businessAddressFinanceInstitute, nameFIRatisbona, bicFIRatisbona);
        }
        else{
            financeInstituteBank = resultList.get(0);
            TypedQuery<FinanceInstitute> query2 = em.createQuery("SELECT s FROM FinanceInstitute s WHERE s.bic=:bicFI", FinanceInstitute.class);
            query2.setParameter("bicFI", bicFIRatisbona);
            this.financeInstituteRatisbona = query2.getResultList().get(0);
        }
        em.flush();
    }

    private void initFIValues(FinanceInstitute fi, Address businessAddress, String name, String bic){
        fi = new FinanceInstitute();
        fi.setAddress(businessAddress);
        fi.setName(name);
        fi.setBic(bic);
        em.persist(fi);
    }

    private void initBankAccount(){
        bankAccountMarketPlace = initBankAccountValues(ibanMarketPlace, null, null, financeInstituteBank);
        bankAccountPersonal = initBankAccountValues(ibanPersonal, bankAccLoginPersonal, bankAccPwdPersonal, financeInstituteRatisbona);
        bankAccountSteamon = initBankAccountValues(ibanSteamon, null, null, financeInstituteBank);
        em.flush();
    }
    private BankAccount initBankAccountValues(String iban, String loginId, String pwd, FinanceInstitute fi){
        // search for init-address. needed after program-restart
        TypedQuery<BankAccount> query = em.createQuery("SELECT s FROM BankAccount s WHERE s.financeInstitute=:fI AND s.iban=:ibanAcc", BankAccount.class);
        query.setParameter("fI", fi);
        query.setParameter("ibanAcc", iban);
        List<BankAccount> resultList = query.getResultList();
        if(resultList.size() == 0){
            BankAccount bankAccount = new BankAccount();
            bankAccount.setFinanceInstitute(fi);
            bankAccount.setIban(iban.trim());
            bankAccount.setBankLoginId(loginId);
            bankAccount.setBankLoginPwd(pwd);
            em.persist(bankAccount);
            return bankAccount;
        }
        return resultList.get(0);
    }
    private void initCustomer(){
        customerPersonal = initCustomerValues(personalAddress, customerPersonalBirthdate, CustomerType.PERSONAL, customerPersonalEmailAddress, customerPersonalFirstName, customerPersonalSecondName);
        customerMarketPlace = initCustomerValues(businessAddressMarketPlace, null, CustomerType.BUSINESS, customerMarketplaceEmailAddress, customerMarketplaceFirstName, customerMarketplaceSecondName);
        customerSteamon = initCustomerValues(businessAddressSteamon, null, CustomerType.BUSINESS, customerSteamonEmailAddress, customerSteamonFirstName, customerSteamonSecondName);
        em.flush();
    }
    private Customer initCustomerValues(Address address, Date birthdate, CustomerType customerType, String email, String firstName, String secondName){
        // search for init-address. needed after program-restart
        TypedQuery<Customer> query = em.createQuery("SELECT s FROM Customer s " +
                "WHERE s.address=:cuAddr AND s.customerType=:cuType AND s.emailAddress=:cuEmail AND s.firstName=:cuFirst and s.secondName=:cuSecond", Customer.class);
        query.setParameter("cuAddr", address);
        query.setParameter("cuType", customerType);
        query.setParameter("cuEmail", email);
        query.setParameter("cuFirst", firstName);
        query.setParameter("cuSecond", secondName);
        List<Customer> resultList = query.getResultList();
        if(resultList.size() == 0){
            Customer customer = new Customer();
            customer.setAddress(address);
            customer.setBirthdate(birthdate);
            customer.setCustomerType(customerType);
            customer.setEmailAddress(email);
            customer.setFirstName(firstName);
            customer.setSecondName(secondName);
            em.persist(customer);
            return customer;
        }
        return resultList.get(0);
    }
    private void initCustomerAccount(){
        customerAccountSteamon = initCustomerAccountValues(customerSteamon, "unHackablePwd", false, true, 2500000,
                new Date(), bankAccountSteamon, null);
        customerAccountMarketplace = initCustomerAccountValues(customerMarketPlace, "securePsWd", false, true, 4500000,
                new Date(), bankAccountMarketPlace, null);
        customerAccountPersonal = initCustomerAccountValues(customerPersonal, "mysecretPwd", true, true, 500000,
                new Date(), bankAccountPersonal, customerSteamon);
        em.flush();
    }
    // clean code would say normally not more than 5 parameters, in this case I let it like this because it's only an init-function
    private CustomerAccount initCustomerAccountValues(Customer customer, String password, Boolean isDirectMoneyTransferAllowed, Boolean isActive,
                                                      int creditInEuroCent, Date firstActivationDate, BankAccount bankAccount, Customer contact){
        // search for init-address. needed after program-restart
        TypedQuery<CustomerAccount> query = em.createQuery("SELECT s FROM CustomerAccount s " +
                "WHERE s.id=:caId", CustomerAccount.class);
        query.setParameter("caId", CustomerAccount.encryptString(customer.getEmailAddress()));
        List<CustomerAccount> resultList = query.getResultList();
        if(resultList.size() == 0){
            CustomerAccount customerAccount = new CustomerAccount(customer.getEmailAddress(), password);
            customerAccount.setDirectMoneyTransferAllowed(isDirectMoneyTransferAllowed);
            customerAccount.setActive(isActive);
            customerAccount.setCreditInEuroCent(creditInEuroCent);
            customerAccount.setFirstActivationDate(firstActivationDate);
            customerAccount.addBankAccount(bankAccount);
            customerAccount.addContact(contact);
            customerAccount.setDefaultBankAccount(bankAccount);
            em.persist(customerAccount);
            return customerAccount;
        }
        else {
            return resultList.get(0);
        }
    }
}
