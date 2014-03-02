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
    }

    public CustomerLookupResponse lookupCustomer(final String customerId)
    {
        CustomerAccountSummary customerAccountSummary = findCustomer(customerId);
        return buildResponse(customerId, customerAccountSummary);
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