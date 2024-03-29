package com.jagodzinski.jhu.ajax.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CustomerServiceAsync
{
	void getPoorestCustomer(AsyncCallback<Customer> callback);

	void getRichestCustomer(AsyncCallback<Customer> callback);

	void redistribute(Integer custId1, Integer custId2, AsyncCallback<List<Customer>> callback);
}