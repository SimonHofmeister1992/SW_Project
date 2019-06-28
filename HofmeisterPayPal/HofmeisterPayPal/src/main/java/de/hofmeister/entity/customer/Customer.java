package de.hofmeister.entity.customer;

import de.hofmeister.entity.address.Address;
import de.hofmeister.entity.util.GeneratedIdEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;


@Entity
@NamedQuery(name = "Customer.all", query = "SELECT s FROM Customer  AS s")
public class Customer extends GeneratedIdEntity {

    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]*(-[A-Z][a-z]*)*$")
    @Size(max = 30)
    private String firstName;
    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]*(-[A-Z][a-z]*)*$")
    @Size(max = 30)
    private String secondName;
    @Temporal(value = TemporalType.DATE)
    private Date birthdate;
    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "^[A-Za-z0-9._-]+@[A-Za-z0-9_-]+.[a-z]{2,3}$")
    @Size(max = 50)
    private String emailAddress;
    @NotNull
    private CustomerType customerType;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @NotNull
    private Address address;
    @Transient
    private String password;



    public Customer() {
    }
    public Customer(String firstName, String secondName, Date birthdate, CustomerType customerType, String emailAddress, Address address) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthdate = birthdate;
        this.emailAddress = emailAddress;
        this.customerType = customerType;
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }
    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        this.password = password;
    }
}
