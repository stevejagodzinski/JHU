package com.jagodzinski.jhu.ajax.controller.simplebanking;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.RequestDispatcher;
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

		String responseFormat = request.getParameter("responseFormat");

		setResponseHeaders(response, responseFormat);
		writeResponse(request, response, customerAccountSummary, responseFormat);
    }

	private void setResponseHeaders(final HttpServletResponse response, final String responseFormat)
	{
		addNoCacheHeaders(response);
		addContentTypeResponseHeader(response, responseFormat);
	}

	private void addNoCacheHeaders(final HttpServletResponse response)
	{
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
	}

	private void addContentTypeResponseHeader(final HttpServletResponse response, final String responseFormat)
	{
		if (responseFormat.equals("json"))
		{
			addJSONContentTypeHeader(response);
		}
		else if (responseFormat.equals("html"))
		{
			addHTMLContentTypeHeader(response);
		}
		else
		{
			throw new IllegalArgumentException("Unsupported responseFormat: " + responseFormat);
		}
	}

	private void addJSONContentTypeHeader(final HttpServletResponse response)
	{
		response.setHeader("Content-type", "application/json");
	}

	private void addHTMLContentTypeHeader(final HttpServletResponse response)
	{
		response.setHeader("Content-type", "text/html");
	}
    
	private void writeResponse(final HttpServletRequest request, final HttpServletResponse response,
			final CustomerAccountSummary customerAccountSummary, final String responseFormat) throws IOException,
			ServletException
	{
		if (responseFormat.equals("json"))
		{
			writeJSONResponse(response, customerAccountSummary);
		}
		else if (responseFormat.equals("html"))
		{
			writeHTMLResponse(request, response, customerAccountSummary);
		}
		else
		{
			throw new IllegalArgumentException("Unsupported responseFormat: " + responseFormat);
		}
	}

	private void writeJSONResponse(final HttpServletResponse response,
			final CustomerAccountSummary customerAccountSummary) throws IOException
	{
		ResponseStrategy responseStrategy = JSONResponseFormatStrategy.getInstance();
		String ajaxResponseBody = responseStrategy.toResponse(customerAccountSummary);
		response.getWriter().write(ajaxResponseBody);
	}

	private void writeHTMLResponse(final HttpServletRequest request, final HttpServletResponse response,
			final CustomerAccountSummary customerAccountSummary) throws ServletException, IOException
	{
		request.setAttribute("firstNameClass", getFirstNameStyleClass(customerAccountSummary));
		request.setAttribute("lastNameClass", getLastNameStyleClass(customerAccountSummary));
		request.setAttribute("accountBalanceNameClass", getAccountBalanceNameStyleClass(customerAccountSummary));
		request.setAttribute("customer", customerAccountSummary);
		String outputPage = "/WEB-INF/customer-lookup-unordered-list-response.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(outputPage);
		dispatcher.include(request, response);
	}

	private String getFirstNameStyleClass(final CustomerAccountSummary customerAccountSummary)
	{
		return customerAccountSummary.getFirstName().equals("Unknown") ? "unknown-name" : "known-name";
	}

	private String getLastNameStyleClass(final CustomerAccountSummary customerAccountSummary)
	{
		return customerAccountSummary.getLastName().equals("Unknown") ? "unknown-name" : "known-name";
	}

	private String getAccountBalanceNameStyleClass(final CustomerAccountSummary customerAccountSummary)
	{
		return customerAccountSummary.getAccountBalance().compareTo(BigDecimal.ZERO) == 0 ? "zero-balance"
				: "non-zero-balance";
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
