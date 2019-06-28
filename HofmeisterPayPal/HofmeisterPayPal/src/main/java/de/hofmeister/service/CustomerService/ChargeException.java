package de.hofmeister.service.CustomerService;

public class ChargeException extends ITransactionException {
    private String message;
    public final static String MONEY_COULD_NOT_BE_CHARGED = "money could not be charged";
    public final static String SENDER_COULD_NOT_BE_IDENTIFIED = "sender konnte nicht identifiziert werden";
    public ChargeException(String message){
        setMessage(message);
    }
    private void setMessage(String message){
        this.message = message;
    }
}
