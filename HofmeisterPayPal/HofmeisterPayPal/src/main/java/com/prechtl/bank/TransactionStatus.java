
package com.prechtl.bank;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für transactionStatus.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="transactionStatus"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NEW"/&gt;
 *     &lt;enumeration value="PENDING"/&gt;
 *     &lt;enumeration value="DONE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "transactionStatus")
@XmlEnum
public enum TransactionStatus {

    NEW,
    PENDING,
    DONE;

    public String value() {
        return name();
    }

    public static TransactionStatus fromValue(String v) {
        return valueOf(v);
    }

}
