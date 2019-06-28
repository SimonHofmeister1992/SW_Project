
package com.prechtl.bank;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für duration.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="duration"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ONCE"/&gt;
 *     &lt;enumeration value="DAILY"/&gt;
 *     &lt;enumeration value="WEEKLY"/&gt;
 *     &lt;enumeration value="MONTHLY"/&gt;
 *     &lt;enumeration value="ANNUALLY"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "duration")
@XmlEnum
public enum Duration {

    ONCE,
    DAILY,
    WEEKLY,
    MONTHLY,
    ANNUALLY;

    public String value() {
        return name();
    }

    public static Duration fromValue(String v) {
        return valueOf(v);
    }

}
