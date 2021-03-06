
package com.prechtl.bank;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.3.1-SNAPSHOT
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "TransactionServiceService", targetNamespace = "http://service/", wsdlLocation = "http://194.95.108.9:8080/prechtlbank-0.1/TransactionService?wsdl")
public class TransactionServiceService
    extends Service
{

    private final static URL TRANSACTIONSERVICESERVICE_WSDL_LOCATION;
    private final static WebServiceException TRANSACTIONSERVICESERVICE_EXCEPTION;
    private final static QName TRANSACTIONSERVICESERVICE_QNAME = new QName("http://service/", "TransactionServiceService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://194.95.108.9:8080/prechtlbank-0.1/TransactionService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        TRANSACTIONSERVICESERVICE_WSDL_LOCATION = url;
        TRANSACTIONSERVICESERVICE_EXCEPTION = e;
    }

    public TransactionServiceService() {
        super(__getWsdlLocation(), TRANSACTIONSERVICESERVICE_QNAME);
    }

    public TransactionServiceService(WebServiceFeature... features) {
        super(__getWsdlLocation(), TRANSACTIONSERVICESERVICE_QNAME, features);
    }

    public TransactionServiceService(URL wsdlLocation) {
        super(wsdlLocation, TRANSACTIONSERVICESERVICE_QNAME);
    }

    public TransactionServiceService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, TRANSACTIONSERVICESERVICE_QNAME, features);
    }

    public TransactionServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TransactionServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns TransactionServiceIF
     */
    @WebEndpoint(name = "TransactionServicePort")
    public TransactionServiceIF getTransactionServicePort() {
        return super.getPort(new QName("http://service/", "TransactionServicePort"), TransactionServiceIF.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns TransactionServiceIF
     */
    @WebEndpoint(name = "TransactionServicePort")
    public TransactionServiceIF getTransactionServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service/", "TransactionServicePort"), TransactionServiceIF.class, features);
    }

    private static URL __getWsdlLocation() {
        if (TRANSACTIONSERVICESERVICE_EXCEPTION!= null) {
            throw TRANSACTIONSERVICESERVICE_EXCEPTION;
        }
        return TRANSACTIONSERVICESERVICE_WSDL_LOCATION;
    }

}
