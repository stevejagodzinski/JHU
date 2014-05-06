package com.jagodzinski.jhu.ajax.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CustomerServiceAsync
{
	void getPoorestCustomer(AsyncCallback<Customer> callback);

	void getRichestCustomer(AsyncCallback<Customer> callback);

	void redistribute(int custId1, int custId2, AsyncCallback<List<Customer>> callback);
}