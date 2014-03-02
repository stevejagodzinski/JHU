package com.jagodzinski.simplebanking.controller;

import com.jagodzinski.simplebanking.model.CustomerAccountSummary;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet("/CustomerLookup")
public class CustomerLookupByIdController extends AbstractCustomerLookupController
{
    @Override
    protected CustomerAccountSummary getCustomerAccountSummary(HttpServletRequest request) {
        String customerId = StringUtils.trimToNull(request.getParameter("customerId"));

        CustomerLookupService customerLookupService = CustomerLookupService.getInstance();
        return customerLookupService.getCustomerById(customerId);
    }
}
