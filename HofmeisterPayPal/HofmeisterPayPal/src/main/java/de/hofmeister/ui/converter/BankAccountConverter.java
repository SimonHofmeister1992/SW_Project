package de.hofmeister.ui.converter;

import de.hofmeister.entity.bank.BankAccount;
import de.hofmeister.service.CustomerService.CustomerService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class BankAccountConverter implements Converter {

    @Inject
    private CustomerService customerService;

    private Map<String, BankAccount> bankAccountMap; // key is the iban

    @PostConstruct
    public void init(){
        if(this.bankAccountMap == null){
            Collection<BankAccount> bankAccounts = customerService.getAllBankAccounts();
            this.bankAccountMap = new HashMap<>();
            bankAccounts.forEach(bankAccount -> {
                bankAccountMap.put(bankAccount.getIban(), bankAccount);
            });
        }
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        String[] split = s.split("-");
        return this.bankAccountMap.get(split[0].trim());
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(this.bankAccountMap.containsKey(o.toString())){
            BankAccount bankAccount = this.bankAccountMap.get(o.toString());
            return "IBAN: " + bankAccount.getIban() + "  -  BIC: " + bankAccount.getFinanceInstitute().getBic() + "  -  Name des Finanzinstituts: " + bankAccount.getFinanceInstitute().getName();
        }
            throw new IllegalArgumentException("Object must be the IBAN-Address but is " + o.toString());
    }

    public void addBankAccount(BankAccount bankAccount){
        if(!bankAccountMap.containsKey(bankAccount.getIban())){
            bankAccountMap.put(bankAccount.getIban(), bankAccount);
        }
    }
}
