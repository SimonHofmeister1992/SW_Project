
package com.prechtl.bank;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für bankAccountStatus.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="bankAccountStatus"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NEW"/&gt;
 *     &lt;enumeration value="PENDING"/&gt;
 *     &lt;enumeration value="APPROVED"/&gt;
 *     &lt;enumeration value="DENIED"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "bankAccountStatus")
@XmlEnum
public enum BankAccountStatus {

    NEW,
    PENDING,
    APPROVED,
    DENIED;

    public String value() {
        return name();
    }

    public static BankAccountStatus fromValue(String v) {
        return valueOf(v);
    }

}
