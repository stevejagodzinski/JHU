package com.jagodzinski.jhu.ajax.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jagodzinski.jhu.ajax.client.Customer;
import com.jagodzinski.jhu.ajax.client.CustomerService;

public class CustomerLookupServiceImpl extends RemoteServiceServlet implements CustomerService
{
	private static final long serialVersionUID = 1L;

	private static Map<Integer, Customer> customers;

	static
    {
        customers = new HashMap<>();

		Customer customerAccountSummary = new Customer();
		customerAccountSummary.setCustomerId(1);
        customerAccountSummary.setFirstName("James");
        customerAccountSummary.setLastName("Gosling");
		customerAccountSummary.setAccountBalance(12345.67);

        customers.put(customerAccountSummary.getCustomerId(), customerAccountSummary);

		customerAccountSummary = new Customer();
		customerAccountSummary.setCustomerId(2);
        customerAccountSummary.setFirstName("Jesse");
        customerAccountSummary.setLastName("James");
		customerAccountSummary.setAccountBalance(891011.12);

        customers.put(customerAccountSummary.getCustomerId(), customerAccountSummary);

		customerAccountSummary = new Customer();
		customerAccountSummary.setCustomerId(3);
        customerAccountSummary.setFirstName("Richie");
        customerAccountSummary.setLastName("Rich");
		customerAccountSummary.setAccountBalance(9999999.99);

        customers.put(customerAccountSummary.getCustomerId(), customerAccountSummary);

		customerAccountSummary = new Customer();
		customerAccountSummary.setCustomerId(4);
        customerAccountSummary.setFirstName("Pointless");
        customerAccountSummary.setLastName("Customer");
		customerAccountSummary.setAccountBalance(123.00);

        customers.put(customerAccountSummary.getCustomerId(), customerAccountSummary);

    }

	public CustomerLookupServiceImpl()
	{
	}

	@Override
	public Customer getRichestCustomer()
	{
		Iterator<Customer> customerIterator = customers.values().iterator();

		Customer richestCustomer = customerIterator.next();

		while (customerIterator.hasNext())
		{
			Customer nextCustomer = customerIterator.next();

			if (nextCustomer.getAccountBalance() > richestCustomer.getAccountBalance())
			{
				richestCustomer = nextCustomer;
			}
		}

		return richestCustomer;
	}

	@Override
	public Customer getPoorestCustomer()
	{
		Iterator<Customer> customerIterator = customers.values().iterator();

		Customer poorestCustomer = customerIterator.next();

		while (customerIterator.hasNext())
		{
			Customer nextCustomer = customerIterator.next();

			if (nextCustomer.getAccountBalance() < poorestCustomer.getAccountBalance())
			{
				poorestCustomer = nextCustomer;
			}
		}

		return poorestCustomer;
	}

	@Override
	public List<Customer> redistribute(int custId1, int custId2)
	{
		Customer customer1 = customers.get(custId1);
		Customer customer2 = customers.get(custId2);

		double averageBalance = (customer1.getAccountBalance() + customer2.getAccountBalance()) / 2.0;

		customer1.setAccountBalance(averageBalance);
		customer2.setAccountBalance(averageBalance);

		return Arrays.asList(customer1, customer2);
	}
}