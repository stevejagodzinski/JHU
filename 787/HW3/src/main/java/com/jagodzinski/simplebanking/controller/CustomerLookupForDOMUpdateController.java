package com.jagodzinski.simplebanking.controller;

import com.jagodzinski.simplebanking.model.CustomerAccountSummary;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/GetHighestBalanceCustomerForUnorderedList")
public class CustomerLookupForDOMUpdateController extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        CustomerAccountSummary highestBalanceCustomer = CustomerLookupService.getInstance().findCustomerWithHighestBalance();

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<li>First Name: ");
        stringBuilder.append(highestBalanceCustomer.getFirstName());
        stringBuilder.append("</li>");

        stringBuilder.append("<li>Last Name: ");
        stringBuilder.append(highestBalanceCustomer.getLastName());
        stringBuilder.append("</li>");

        stringBuilder.append("<li>Account Balance: ");
        stringBuilder.append(highestBalanceCustomer.getAccountBalance());
        stringBuilder.append("</li>");

        response.getWriter().append(stringBuilder.toString());
    }
}
