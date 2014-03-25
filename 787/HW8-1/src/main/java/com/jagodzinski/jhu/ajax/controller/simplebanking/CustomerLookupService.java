package com.jagodzinski.jhu.ajax.controller.simplebanking;

import com.jagodzinski.jhu.ajax.model.simplebanking.CustomerAccountSummary;

import java.math.BigDecimal;
import java.util.*;

public class CustomerLookupService
{
    private Map<String, CustomerAccountSummary> customers;

    private CustomerLookupService()
    {
        initializeCustomerData();
    }

    public static CustomerLookupService getInstance()
    {
        return new CustomerLookupService();
    }

    private void initializeCustomerData()
    {
        customers = new HashMap<>();

        CustomerAccountSummary customerAccountSummary = CustomerAccountSummary.newInstance();
        customerAccountSummary.setCustomerId("a1");
        customerAccountSummary.setFirstName("James");
        customerAccountSummary.setLastName("Gosling");
        customerAccountSummary.setAccountBalance(new BigDecimal("12345.67"));

        customers.put(customerAccountSummary.getCustomerId(), customerAccountSummary);

        customerAccountSummary = CustomerAccountSummary.newInstance();
        customerAccountSummary.setCustomerId("a2");
        customerAccountSummary.setFirstName("Jesse");
        customerAccountSummary.setLastName("James");
        customerAccountSummary.setAccountBalance(new BigDecimal("891011.12"));

        customers.put(customerAccountSummary.getCustomerId(), customerAccountSummary);

        customerAccountSummary = CustomerAccountSummary.newInstance();
        customerAccountSummary.setCustomerId("a3");
        customerAccountSummary.setFirstName("Richie");
        customerAccountSummary.setLastName("Rich");
        customerAccountSummary.setAccountBalance(new BigDecimal("9999999.99"));

        customers.put(customerAccountSummary.getCustomerId(), customerAccountSummary);

        customerAccountSummary = CustomerAccountSummary.newInstance();
        customerAccountSummary.setCustomerId("a4");
        customerAccountSummary.setFirstName("Pointless");
        customerAccountSummary.setLastName("Customer");
        customerAccountSummary.setAccountBalance(new BigDecimal("123.00"));

        customers.put(customerAccountSummary.getCustomerId(), customerAccountSummary);
    }

    public Collection<CustomerAccountSummary> getAllCustomers(final Collection<String> customerIds) {
    	List<CustomerAccountSummary> customerAccountSummaries = new ArrayList<>(customerIds.size());
    	
    	for(String customerId : customerIds) {
    		customerAccountSummaries.add(getCustomerById(customerId));
    	}
    	
    	return customerAccountSummaries;
    }
    
    public CustomerAccountSummary getCustomerById(final String customerId)
    {
        return customers.get(customerId);
    }

    public List<String> getCustomerIds()
    {
        List<String> customerIds = new ArrayList<>(customers.keySet());
        Collections.sort(customerIds);
        return Collections.unmodifiableList(customerIds);
    }

    public CustomerAccountSummary getCustomerByName(final String firstName, final String lastName)
    {
        CustomerAccountSummary returnValue = null;

        for(CustomerAccountSummary customer : customers.values())
        {
            if(customer.getFirstName().equalsIgnoreCase(firstName) && customer.getLastName().equalsIgnoreCase(lastName))
            {
                returnValue = customer;
                break;
            }
        }

        return returnValue;
    }
}