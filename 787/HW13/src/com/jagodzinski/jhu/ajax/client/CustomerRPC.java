package com.jagodzinski.jhu.ajax.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class CustomerRPC implements EntryPoint
{
	private CustomerServiceAsync serviceProxy;

	private Integer richestCustomerId;
	private Integer poorestCustomerId;

	private HTML richestCustomerHTML;
	private HTML poorestCustomerHTML;
	private HTML redistributionResultHTML;

	public void onModuleLoad()
	{
		serviceProxy = GWT.create(CustomerService.class);

		Button getRichestCustomerButton = new Button("Get Richest Customer");
		richestCustomerHTML = new HTML("<i>Richest Customer Will Go Here</i>");
		getRichestCustomerButton.addClickHandler(new RichestCustomerButtonHandler());

		RootPanel.get("get-richest").add(getRichestCustomerButton);
		RootPanel.get("get-richest").add(richestCustomerHTML);

		Button getPoorestCustomerButton = new Button("Get Poorest Customer");
		poorestCustomerHTML = new HTML("<i>Poorest Customer Will Go Here</i>");
		getPoorestCustomerButton.addClickHandler(new PoorestCustomerButtonHandler());

		RootPanel.get("get-poorest").add(getPoorestCustomerButton);
		RootPanel.get("get-poorest").add(poorestCustomerHTML);

		Button redistributeButton = new Button("Redistribute");
		redistributionResultHTML = new HTML("<i>Redistribution Result Will Go Here</i>");
		redistributeButton.addClickHandler(new RedistributeButtonHandler());
		RootPanel.get("redistribute").add(redistributeButton);
		RootPanel.get("redistribute").add(redistributionResultHTML);
	}

	private class RichestCustomerButtonHandler implements ClickHandler
	{
		@Override
		public void onClick(ClickEvent event)
		{
			serviceProxy.getRichestCustomer(new GetRichestCustomerCallback());
		}
	}

	private class PoorestCustomerButtonHandler implements ClickHandler
	{
		@Override
		public void onClick(ClickEvent event)
		{
			serviceProxy.getPoorestCustomer(new GetPoorestCustomerCallback());
		}
	}

	private class RedistributeButtonHandler implements ClickHandler
	{
		@Override
		public void onClick(ClickEvent event)
		{
			serviceProxy.redistribute(richestCustomerId, poorestCustomerId, new RedistributeCustomerBalancesCallback());
		}
	}

	private class GetRichestCustomerCallback implements AsyncCallback<Customer>
	{
		@Override
		public void onSuccess(Customer result)
		{
			richestCustomerId = result.getCustomerId();
			richestCustomerHTML.setHTML(result.toString());
		}

		@Override
		public void onFailure(Throwable caught)
		{
			Window.alert("Unable to get data from server.");
		}
	}

	private class GetPoorestCustomerCallback implements AsyncCallback<Customer>
	{
		@Override
		public void onSuccess(Customer result)
		{
			poorestCustomerId = result.getCustomerId();
			poorestCustomerHTML.setHTML(result.toString());
		}

		@Override
		public void onFailure(Throwable caught)
		{
			Window.alert("Unable to get data from server.");
		}
	}

	private class RedistributeCustomerBalancesCallback implements AsyncCallback<List<Customer>>
	{
		@Override
		public void onSuccess(List<Customer> customers)
		{
			String result = "";

			for (Customer customer : customers)
			{
				result += customer + "<br />";
			}

			redistributionResultHTML.setHTML(result);
		}

		@Override
		public void onFailure(Throwable caught)
		{
			Window.alert("Unable to re-distribute richest and poorest customer balances. Be sure to \"Get Richest\" and \"Get Poorest\" first!");
		}
	}
}
