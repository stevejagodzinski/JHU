package com.jagodzinski.simplebanking.controller;

import com.jagodzinski.simplebanking.model.CustomerAccountSummary;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractCustomerLookupController extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        CustomerAccountSummary customerAccountSummary = getCustomerAccountSummary(request);

        setNoCacheResponseHeaders(response);

        String address;

        if(customerAccountSummary != null)
        {
            request.setAttribute("customerAccountSummary", customerAccountSummary);
            address = "/WEB-INF/customer-lookup/response/CustomerLookupResponse.jsp";
        }
        else
        {
            address = "/WEB-INF/customer-lookup/response/CustomerLookupError.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }

    private void setNoCacheResponseHeaders(HttpServletResponse response)
    {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
    }

    protected abstract CustomerAccountSummary getCustomerAccountSummary(HttpServletRequest request);
}
