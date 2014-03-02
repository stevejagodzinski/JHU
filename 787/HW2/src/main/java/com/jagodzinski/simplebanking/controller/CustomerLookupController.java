package com.jagodzinski.simplebanking.controller;

import com.jagodzinski.simplebanking.model.CustomerLookupResponse;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/CustomerLookup")
public class CustomerLookupController extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String customerId = StringUtils.trimToNull(request.getParameter("customerId"));

        CustomerLookupResponse customerLookupResponse = CustomerLookupService.getInstance().lookupCustomer(customerId);

        request.setAttribute("customerLookupResponse", customerLookupResponse);

        String address;

        if(customerLookupResponse.isSuccess())
        {
            address = "/WEB-INF/customer-lookup/response/CustomerLookupResponse.jsp";
        }
        else
        {
            address = "/WEB-INF/customer-lookup/response/CustomerLookupError.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
