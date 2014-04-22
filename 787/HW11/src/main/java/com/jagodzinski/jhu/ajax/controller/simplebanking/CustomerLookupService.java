package com.jagodzinski.jhu.ajax.controller.simplebanking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Collection<CustomerAccountSummary> getAllCustomers(final Collection<String> customerIds) {
    	List<CustomerAccountSummary> customerAccountSummaries = new ArrayList<>(customerIds.size());
    	
    	for(String customerId : customerIds) {
    		customerAccountSummaries.add(getCustomerById(customerId));
    	}
    	
    	return customerAccountSummaries;
    }
    
    public CustomerAccountSummary getCustomerById(final String customerId)
    {
		CustomerAccountSummary customer = customers.get(customerId);

		if (customer == null)
		{
			customer = CustomerAccountSummary.newUnknownCustomerInstance(customerId);
		}

		return customer;
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

	public List<String> getAllCustomerIdsStartingWith(String prefix)
	{
		prefix = prefix.toUpperCase();

		List<String> matches = new ArrayList<>();

		for (CustomerAccountSummary customer : customers.values())
		{
			String customerId = customer.getCustomerId();
			if (customerId.toUpperCase().startsWith(prefix))
			{
				matches.add(customerId);
			}
		}

		return matches;
	}

	public CustomerAccountSummary findCustomerWithHighestBalance()
	{
		CustomerAccountSummary highestBalanceCustomer = null;

		for (CustomerAccountSummary customer : customers.values())
		{
			if (hasHigherBalance(highestBalanceCustomer, customer))
			{
				highestBalanceCustomer = customer;
			}
		}

		return highestBalanceCustomer;
	}

	private boolean hasHigherBalance(CustomerAccountSummary highestBalanceCustomer, CustomerAccountSummary toCheck)
	{
		return highestBalanceCustomer == null
				|| toCheck.getAccountBalance().compareTo(highestBalanceCustomer.getAccountBalance()) > 0;
	}
}