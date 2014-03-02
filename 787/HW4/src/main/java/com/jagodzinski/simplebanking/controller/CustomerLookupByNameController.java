package com.jagodzinski.simplebanking.controller;

import com.jagodzinski.simplebanking.model.CustomerAccountSummary;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet("/LookupCustomerByName")
public class CustomerLookupByNameController extends AbstractCustomerLookupController
{
    @Override
    protected CustomerAccountSummary getCustomerAccountSummary(HttpServletRequest request) {
        String firstName = StringUtils.trimToNull(request.getParameter("firstName"));
        String lastName = StringUtils.trimToNull(request.getParameter("lastName"));

        CustomerLookupService service = CustomerLookupService.getInstance();
        return service.getCustomerByName(firstName, lastName);
    }
}
