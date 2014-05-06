package com.jagodzinski.jhu.ajax.client;

import java.io.Serializable;

public class Customer implements Serializable
{
	private static final long serialVersionUID = -1L;

	public Customer()
	{
	}

	private double accountBalance;
	private int customerId;
    private String firstName;
    private String lastName;

	public double getAccountBalance()
    {
        return accountBalance;
    }

	public void setAccountBalance(final double accountBalance)
    {
        this.accountBalance = accountBalance;
    }

	public int getCustomerId()
    {
        return customerId;
    }

	public void setCustomerId(final int customerId)
    {
        this.customerId = customerId;
    }

    public String getFirstName()
    {
        return firstName;
    }

	public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

	public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }

	public String toString()
	{
		return "Customer ID: " + customerId + ", First Name: " + firstName + ", Last Name: " + lastName
				+ ", Account Balance: " + accountBalance;
	}
}