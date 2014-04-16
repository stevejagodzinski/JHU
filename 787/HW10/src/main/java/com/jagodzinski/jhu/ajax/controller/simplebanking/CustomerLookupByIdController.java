package com.jagodzinski.jhu.ajax.controller.simplebanking;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.jagodzinski.jhu.ajax.controller.dataformat.JSONResponseFormatStrategy;
import com.jagodzinski.jhu.ajax.controller.dataformat.ResponseStrategy;
import com.jagodzinski.jhu.ajax.model.simplebanking.CustomerAccountSummary;

@WebServlet("/GetCustomerByID")
public class CustomerLookupByIdController extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		CustomerAccountSummary customerAccountSummary = getCustomerAccountSummary(request);

		setResponseHeaders(response);

		ResponseStrategy responseStrategy = JSONResponseFormatStrategy.getInstance();
		String ajaxResponseBody = responseStrategy.toResponse(customerAccountSummary);
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
    
	private CustomerAccountSummary getCustomerAccountSummary(HttpServletRequest request) {
		String customerId = getCustomerId(request);

        CustomerLookupService customerLookupService = CustomerLookupService.getInstance();
		return customerLookupService.getCustomerById(customerId);
    }
    
	private String getCustomerId(HttpServletRequest request) {
		return StringUtils.trimToNull(request.getParameter("customerId"));
    }
}
