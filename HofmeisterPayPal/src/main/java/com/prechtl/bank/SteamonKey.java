
package com.prechtl.bank;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für steamonKey complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="steamonKey"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://service/}generatedIdEntity"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="keyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="keyReceiver" type="{http://service/}user" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "steamonKey", propOrder = {
    "keyCode",
    "keyReceiver"
})
public class SteamonKey
    extends GeneratedIdEntity
{

    protected String keyCode;
    protected User keyReceiver;

    /**
     * Ruft den Wert der keyCode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyCode() {
        return keyCode;
    }

    /**
     * Legt den Wert der keyCode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyCode(String value) {
        this.keyCode = value;
    }

    /**
     * Ruft den Wert der keyReceiver-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link User }
     *     
     */
    public User getKeyReceiver() {
        return keyReceiver;
    }

    /**
     * Legt den Wert der keyReceiver-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link User }
     *     
     */
    public void setKeyReceiver(User value) {
        this.keyReceiver = value;
    }

}
