
package com.prechtl.bank;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.prechtl.bank package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Address_QNAME = new QName("http://service/", "address");
    private final static QName _DirectDebit_QNAME = new QName("http://service/", "directDebit");
    private final static QName _DirectDebitResponse_QNAME = new QName("http://service/", "directDebitResponse");
    private final static QName _LoginDTO_QNAME = new QName("http://service/", "loginDTO");
    private final static QName _TransactionDTO_QNAME = new QName("http://service/", "transactionDTO");
    private final static QName _Transfer_QNAME = new QName("http://service/", "transfer");
    private final static QName _TransferResponse_QNAME = new QName("http://service/", "transferResponse");
    private final static QName _TransactionException_QNAME = new QName("http://service/", "TransactionException");
    private final static QName _LoginException_QNAME = new QName("http://service/", "LoginException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.prechtl.bank
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Address }
     * 
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link DirectDebit }
     * 
     */
    public DirectDebit createDirectDebit() {
        return new DirectDebit();
    }

    /**
     * Create an instance of {@link DirectDebitResponse }
     * 
     */
    public DirectDebitResponse createDirectDebitResponse() {
        return new DirectDebitResponse();
    }

    /**
     * Create an instance of {@link LoginDTO }
     * 
     */
    public LoginDTO createLoginDTO() {
        return new LoginDTO();
    }

    /**
     * Create an instance of {@link TransactionDTO }
     * 
     */
    public TransactionDTO createTransactionDTO() {
        return new TransactionDTO();
    }

    /**
     * Create an instance of {@link Transfer }
     * 
     */
    public Transfer createTransfer() {
        return new Transfer();
    }

    /**
     * Create an instance of {@link TransferResponse }
     * 
     */
    public TransferResponse createTransferResponse() {
        return new TransferResponse();
    }

    /**
     * Create an instance of {@link TransactionException }
     * 
     */
    public TransactionException createTransactionException() {
        return new TransactionException();
    }

    /**
     * Create an instance of {@link LoginException }
     * 
     */
    public LoginException createLoginException() {
        return new LoginException();
    }

    /**
     * Create an instance of {@link BankAccount }
     * 
     */
    public BankAccount createBankAccount() {
        return new BankAccount();
    }

    /**
     * Create an instance of {@link GeneratedIdEntity }
     * 
     */
    public GeneratedIdEntity createGeneratedIdEntity() {
        return new GeneratedIdEntity();
    }

    /**
     * Create an instance of {@link BankInstitute }
     * 
     */
    public BankInstitute createBankInstitute() {
        return new BankInstitute();
    }

    /**
     * Create an instance of {@link Transaction }
     * 
     */
    public Transaction createTransaction() {
        return new Transaction();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link SteamonKey }
     * 
     */
    public SteamonKey createSteamonKey() {
        return new SteamonKey();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Address }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Address }{@code >}
     */
    @XmlElementDecl(namespace = "http://service/", name = "address")
    public JAXBElement<Address> createAddress(Address value) {
        return new JAXBElement<Address>(_Address_QNAME, Address.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DirectDebit }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DirectDebit }{@code >}
     */
    @XmlElementDecl(namespace = "http://service/", name = "directDebit")
    public JAXBElement<DirectDebit> createDirectDebit(DirectDebit value) {
        return new JAXBElement<DirectDebit>(_DirectDebit_QNAME, DirectDebit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DirectDebitResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DirectDebitResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service/", name = "directDebitResponse")
    public JAXBElement<DirectDebitResponse> createDirectDebitResponse(DirectDebitResponse value) {
        return new JAXBElement<DirectDebitResponse>(_DirectDebitResponse_QNAME, DirectDebitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginDTO }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LoginDTO }{@code >}
     */
    @XmlElementDecl(namespace = "http://service/", name = "loginDTO")
    public JAXBElement<LoginDTO> createLoginDTO(LoginDTO value) {
        return new JAXBElement<LoginDTO>(_LoginDTO_QNAME, LoginDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionDTO }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransactionDTO }{@code >}
     */
    @XmlElementDecl(namespace = "http://service/", name = "transactionDTO")
    public JAXBElement<TransactionDTO> createTransactionDTO(TransactionDTO value) {
        return new JAXBElement<TransactionDTO>(_TransactionDTO_QNAME, TransactionDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Transfer }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Transfer }{@code >}
     */
    @XmlElementDecl(namespace = "http://service/", name = "transfer")
    public JAXBElement<Transfer> createTransfer(Transfer value) {
        return new JAXBElement<Transfer>(_Transfer_QNAME, Transfer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransferResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service/", name = "transferResponse")
    public JAXBElement<TransferResponse> createTransferResponse(TransferResponse value) {
        return new JAXBElement<TransferResponse>(_TransferResponse_QNAME, TransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransactionException }{@code >}
     */
    @XmlElementDecl(namespace = "http://service/", name = "TransactionException")
    public JAXBElement<TransactionException> createTransactionException(TransactionException value) {
        return new JAXBElement<TransactionException>(_TransactionException_QNAME, TransactionException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LoginException }{@code >}
     */
    @XmlElementDecl(namespace = "http://service/", name = "LoginException")
    public JAXBElement<LoginException> createLoginException(LoginException value) {
        return new JAXBElement<LoginException>(_LoginException_QNAME, LoginException.class, null, value);
    }

}
