package de.hofmeister.entity.address;

import javax.persistence.Embeddable;
import javax.validation.ValidationException;
import javax.validation.constraints.Size;

@Embeddable
public class Country {
    @Size(max=60)
    private String countryName;
    @Size(max=2)
    private String countryIsoAlpha2;

    public Country(){

    }
    public Country(String iso3166AlphaTwoCode) throws ValidationException {
        setCountryIsoAlpha2(iso3166AlphaTwoCode);
    }
    public Country(String iso3166AlphaTwoCode, String countryName) throws ValidationException {
        setCountryIsoAlpha2(iso3166AlphaTwoCode);
        setName(countryName);
    }

    public String getCountryIsoAlpha2(){
        return this.countryIsoAlpha2;
    }
    public void setCountryIsoAlpha2(String iso3166AlphaTwoCode) throws ValidationException {
        if (iso3166AlphaTwoCode.length() == 2) {
            countryIsoAlpha2 = iso3166AlphaTwoCode.toUpperCase();
        } else {
            throw new ValidationException("country code must contain exactly two characters");
        }
    }

    public String getName() {
        return countryName;
    }
    public void setName(String countryName) {
        countryName = countryName.substring(0, 1).toUpperCase() + countryName.substring(1).toLowerCase().trim();
        this.countryName = countryName;
    }
}
