package de.hofmeister.entity.address;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Embeddable
public class City {

    @Size(max = 60)
    @Pattern(regexp = "^[A-Z][a-z]*(-[A-Z][a-z]*)*$")
    private String cityName;
    @NotNull
    private Country country;
    @NotNull
    private String zipCode;

    public City() {

    }

    public City(String zipCode, Country country) {
        this.zipCode = zipCode;
        this.setCountry(country);
    }
    public City(String zipCode, Country country, String cityName) {
        this.zipCode = zipCode;
        this.setCountry(country);
        this.setName(cityName);
    }

    public Country getCountry() {
        return country;
    }
    public void setCountry(Country country) {
        country.setCountryIsoAlpha2(country.getCountryIsoAlpha2().toUpperCase());
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getName() {
        return cityName;
    }
    public void setName(String cityName) {
        this.cityName = cityName;
    }
}
