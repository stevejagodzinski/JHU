package com.jagodzinski.simplebanking.model;

public class CustomerLookupResponse
{
    public static CustomerLookupResponse newInstance()
    {
        return new CustomerLookupResponse();
    }

    private CustomerLookupResponse(){};

    private boolean success;

    private String customerId;

    private CustomerAccountSummary customerAccountSummary;

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }


    public CustomerAccountSummary getCustomerAccountSummary()
    {
        return customerAccountSummary;
    }

    public void setCustomerAccountSummary(CustomerAccountSummary customerAccountSummary)
    {
        this.customerAccountSummary = customerAccountSummary;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
}