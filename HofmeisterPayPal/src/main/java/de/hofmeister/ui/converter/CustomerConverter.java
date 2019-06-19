package de.hofmeister.ui.converter;

import de.hofmeister.entity.customer.Customer;
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
public class CustomerConverter implements Converter {

    @Inject
    private CustomerService customerService;

    private Map<String, Customer> customerMap; // mapping via email-address
    private Map<String, Customer> customerObjectMap; // mapping via objectid

    @PostConstruct
    public void init(){
        Collection<Customer> customers = null;
        if(this.customerMap == null || customerObjectMap == null){
            customers = customerService.getAllCustomers();
            this.customerMap = new HashMap<>();
            this.customerObjectMap = new HashMap<>();
        }
            customers.forEach(customer -> {
                customerMap.put(customer.getEmailAddress(), customer);
                customerObjectMap.put("de.hofmeister.entity.customer.Customer@" + customer.getId(), customer);
            });

    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return customerObjectMap.get(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o.getClass().equals(Customer.class)){
            o= ((Customer) o).getEmailAddress();
            if(this.customerMap.containsKey(o.toString())) {
                Customer customer = this.customerMap.get(o.toString());
                return customer.toString();
            }
        }
        if(this.customerMap.containsKey(o.toString())){
            Customer customer = this.customerMap.get(o.toString());
            return customer.getEmailAddress() + "  -  " + customer.getFirstName() + "  -  " + customer.getSecondName();
        }
            throw new IllegalArgumentException("Object must be the Email-Address but is " + o.toString());
    }

}
