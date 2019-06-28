package de.hofmeister.entity.address;

import de.hofmeister.entity.util.GeneratedIdEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Address extends GeneratedIdEntity {

    @NotNull
    private String street;
    @NotNull
    private String houseNumber;
    private String firstStreetAddition;
    private String secondStreetAddition;
    @NotNull
    private City city;

    public Address() {

    }
    public Address(City city, String street, String houseNumber) {
        setCity(city);
        setStreet(street);
        setHouseNumber(houseNumber);
    }
    public Address(City city, String street, String houseNumber, String firstStreetAddition) {
        setCity(city);
        setStreet(street);
        setHouseNumber(houseNumber);
        setFirstStreetAddition(firstStreetAddition);
    }
    public Address(City city, String street, String houseNumber, String firstStreetAddition, String secondStreetAddition) {
        setCity(city);
        setStreet(street);
        setHouseNumber(houseNumber);
        setFirstStreetAddition(firstStreetAddition);
        setSecondStreetAddition(secondStreetAddition);
    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getFirstStreetAddition() {
        return firstStreetAddition;
    }
    public void setFirstStreetAddition(String firstStreetAddition) {
        this.firstStreetAddition = firstStreetAddition;
    }

    public String getSecondStreetAddition() {
        return secondStreetAddition;
    }
    public void setSecondStreetAddition(String secondStreetAddition) {
        this.secondStreetAddition = secondStreetAddition;
    }

    public City getCity() {
        return city;
    }
    public void setCity(City city) {
        this.city = city;
    }
}
