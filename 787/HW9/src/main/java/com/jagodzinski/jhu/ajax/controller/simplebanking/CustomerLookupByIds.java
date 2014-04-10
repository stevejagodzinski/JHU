package com.jagodzinski.jhu.ajax.controller.simplebanking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.jagodzinski.jhu.ajax.controller.dataformat.JSONResponseFormatStrategy;
import com.jagodzinski.jhu.ajax.controller.dataformat.ResponseStrategy;
import com.jagodzinski.jhu.ajax.model.simplebanking.CustomerAccountSummary;

@WebServlet("/GetCustomersByIDs")
public class CustomerLookupByIds extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (shouldSimlateLongRunningServerResponse(request))
		{
			simlateLongRunningServerResponse();
		}

		Collection<CustomerAccountSummary> customerAccountSummaries = getCustomerAccountSummaries(request);

		setResponseHeaders(response);

		ResponseStrategy responseStrategy = JSONResponseFormatStrategy.getInstance();
		String ajaxResponseBody = responseStrategy.toResponse(customerAccountSummaries);
		response.getWriter().write(ajaxResponseBody);
	}

	private void setResponseHeaders(final HttpServletResponse response)
	{
		addNoCacheHeaders(response);
		addJSONContentTypeHeader(response);
	}

	private void addNoCacheHeaders(final HttpServletResponse response)
	{
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
	}

	private void addJSONContentTypeHeader(final HttpServletResponse response)
	{
		response.setHeader("Content-type", "application/json");
	}

	private Collection<CustomerAccountSummary> getCustomerAccountSummaries(HttpServletRequest request)
	{
		List<String> customerIds = getCustomerIds(request);

		CustomerLookupService customerLookupService = CustomerLookupService.getInstance();
		return customerLookupService.getAllCustomers(customerIds);
	}

	private List<String> getCustomerIds(final HttpServletRequest request)
	{
		List<String> customerIds = Arrays.asList(StringUtils.split(request.getParameter("customerIds"), ','));
		List<String> trimmedCustomerIds = new ArrayList<>(customerIds.size());

		for (String customerId : customerIds)
		{
			trimmedCustomerIds.add(customerId.trim());
		}

		return trimmedCustomerIds;
	}

	private boolean shouldSimlateLongRunningServerResponse(final HttpServletRequest request)
	{
		return StringUtils.equalsIgnoreCase(request.getParameter("simlateLongRunningServerResponse"),
				Boolean.TRUE.toString());
	}

	private void simlateLongRunningServerResponse()
	{
		try
		{
			Thread.sleep(TimeUnit.SECONDS.toMillis(5));
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}
}
