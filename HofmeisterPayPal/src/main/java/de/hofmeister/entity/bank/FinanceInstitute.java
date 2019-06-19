package de.hofmeister.entity.bank;

import de.hofmeister.entity.address.Address;
import de.hofmeister.entity.util.GeneratedIdEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class FinanceInstitute extends GeneratedIdEntity {
    @NotNull
    @Pattern(regexp = "^[A-Z0-9]{8}$|^[A-Z0-9]{11}$")
    private String bic;
    @NotNull
    private String name;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
    private Address address;

    public FinanceInstitute() {

    }
    public FinanceInstitute(String bic, String name, Address address) {
        this.bic = bic;
        this.address = address;
        this.name = name;
    }

    public String getBic() {
        return bic;
    }
    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
}
