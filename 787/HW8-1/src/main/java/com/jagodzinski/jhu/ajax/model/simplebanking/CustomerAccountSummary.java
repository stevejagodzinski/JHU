package com.jagodzinski.jhu.ajax.model.simplebanking;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
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

	@XmlElement
    public BigDecimal getAccountBalance()
    {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance)
    {
        this.accountBalance = accountBalance;
    }

	@XmlElement
    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(final String customerId)
    {
        this.customerId = customerId;
    }

	@XmlElement
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

	@XmlElement
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
}