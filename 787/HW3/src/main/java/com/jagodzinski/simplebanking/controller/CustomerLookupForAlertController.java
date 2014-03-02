package com.jagodzinski.simplebanking.controller;

import com.jagodzinski.simplebanking.model.CustomerAccountSummary;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/GetHighestBalanceCustomerForAlert")
public class CustomerLookupForAlertController extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        CustomerAccountSummary highestBalanceCustomer = CustomerLookupService.getInstance().findCustomerWithHighestBalance();

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");

        PrintWriter out = response.getWriter();

        StringBuilder stringBuilder = new StringBuilder();

        out.print("First Name: ");
        out.println(highestBalanceCustomer.getFirstName());

        out.print("Last Name: ");
        out.println(highestBalanceCustomer.getLastName());

        out.print("Account Balance: ");
        out.println(highestBalanceCustomer.getAccountBalance());
    }
}
