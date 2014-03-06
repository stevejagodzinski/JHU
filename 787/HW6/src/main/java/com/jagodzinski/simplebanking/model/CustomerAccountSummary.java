package com.jagodzinski.simplebanking.model;

import java.math.BigDecimal;

public class CustomerAccountSummary
{
    private CustomerAccountSummary(){};

    public static CustomerAccountSummary newInstance()
    {
        return new CustomerAccountSummary();
    }

    private BigDecimal accountBalance;
    private String customerId;
    private String firstName;
    private String lastName;

    public BigDecimal getAccountBalance()
    {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance)
    {
        this.accountBalance = accountBalance;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(final String customerId)
    {
        this.customerId = customerId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
}