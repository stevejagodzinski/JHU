package com.jagodzinski.simplebanking.controller;

import com.jagodzinski.simplebanking.model.CustomerAccountSummary;
import com.jagodzinski.simplebanking.model.CustomerLookupResponse;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    public CustomerLookupResponse lookupCustomer(final String customerId)
    {
        CustomerAccountSummary customerAccountSummary = findCustomer(customerId);
        return buildResponse(customerId, customerAccountSummary);
    }

    public CustomerAccountSummary findCustomerWithHighestBalance()
    {
        CustomerAccountSummary highestBalanceCustomer = null;

        for(CustomerAccountSummary customer : customers.values())
        {
            if(hasHigherBalance(highestBalanceCustomer, customer))
            {
                highestBalanceCustomer = customer;
            }
        }

        return highestBalanceCustomer;
    }

    private boolean hasHigherBalance(CustomerAccountSummary highestBalanceCustomer, CustomerAccountSummary toCheck)
    {
        return highestBalanceCustomer == null || toCheck.getAccountBalance().compareTo(highestBalanceCustomer.getAccountBalance()) > 0;
    }

    private CustomerAccountSummary findCustomer(final String customerId)
    {
        return customers.get(customerId);
    }

    private CustomerLookupResponse buildResponse(final String requestedCustomerId, final CustomerAccountSummary customerAccountSummary)
    {
        CustomerLookupResponse response = CustomerLookupResponse.newInstance();
        response.setCustomerAccountSummary(customerAccountSummary);
        response.setCustomerId(requestedCustomerId);
        response.setSuccess(customerAccountSummary != null);
        return response;
    }
}