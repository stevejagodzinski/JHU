package com.jagodzinski.jhu.ajax.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("customer-service")
public interface CustomerService extends RemoteService
{
	Customer getRichestCustomer();

	Customer getPoorestCustomer();

	List<Customer> redistribute(Integer custId1, Integer custId2);
}