package com.jagodzinski.jhu.ajax.controller.simplebanking;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.jagodzinski.jhu.ajax.model.simplebanking.CustomerAccountSummary;

@WebServlet("/UpdateCustomerName")
public class UpdateCustomerNameController extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		CustomerAccountSummary customerAccountSummary = getCustomerAccountSummary(request);

		String updatedName = setCustomerName(customerAccountSummary, request);

		setResponseHeaders(response);

		response.getWriter().write(updatedName);
    }

	private String setCustomerName(CustomerAccountSummary customerAccountSummary, HttpServletRequest request)
	{
		String firstName = request.getParameter("value");

		customerAccountSummary.setFirstName(firstName);

		return firstName;
	}

	private void setResponseHeaders(final HttpServletResponse response)
	{
		addNoCacheHeaders(response);
	}

	private void addNoCacheHeaders(final HttpServletResponse response)
	{
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
	}
    
	private CustomerAccountSummary getCustomerAccountSummary(HttpServletRequest request) {
		String customerId = getCustomerId(request);

        CustomerLookupService customerLookupService = CustomerLookupService.getInstance();
		return customerLookupService.getCustomerById(customerId);
    }
    
	private String getCustomerId(HttpServletRequest request) {
		return StringUtils.trimToNull(request.getParameter("customerId"));
    }
}
