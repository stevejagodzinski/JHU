package com.jagodzinski.jhu.ajax.controller.simplebanking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.jagodzinski.jhu.ajax.model.simplebanking.CustomerAccountSummary;

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

	public CustomerAccountSummary[] getAllCustomers(final String[] customerIds) {
		return getAllCustomers(Arrays.asList(customerIds)).toArray(new CustomerAccountSummary[0]);
	}

	private Collection<CustomerAccountSummary> getAllCustomers(final Collection<String> customerIds) {
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

	public boolean addCustomer(JSONObject jsonCustomer) {
		boolean success;

		try {
			String customerId = jsonCustomer.getString("customerId");

			if (customers.containsKey(customerId)) {
				success = false;
			} else {
				CustomerAccountSummary newCustomer = createCustomerAccountSummary(jsonCustomer);
				customers.put(customerId, newCustomer);
				success = true;
			}
		} catch (JSONException e) {
			success = false;
		}

		return success;
	}

	private CustomerAccountSummary createCustomerAccountSummary(JSONObject jsonCustomer) throws JSONException {
		CustomerAccountSummary newCustomer = CustomerAccountSummary.newInstance();
		newCustomer.setAccountBalance(new BigDecimal(jsonCustomer.getString("accountBalance")));
		newCustomer.setCustomerId(jsonCustomer.getString("customerId"));
		newCustomer.setFirstName(jsonCustomer.getString("firstName"));
		newCustomer.setLastName(jsonCustomer.getString("lastName"));
		return newCustomer;
	}
}