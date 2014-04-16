package com.jagodzinski.jhu.ajax.controller.simplebanking;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetCustomerIds")
public class GetCustomerIds extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		setResponseHeaders(response);

		String inputCity = request.getParameter("customerIdPrefix");
		request.setAttribute("items", findMatchingCustomerIds(inputCity));
		String outputPage = "/WEB-INF/items-list.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(outputPage);
		dispatcher.include(request, response);
	}

	private List<String> findMatchingCustomerIds(String customerIdPrefix)
	{
		return CustomerLookupService.getInstance().getAllCustomerIdsStartingWith(customerIdPrefix);
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
}
