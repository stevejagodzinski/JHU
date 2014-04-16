package com.jagodzinski.jhu.ajax.controller.cities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetCities")
public class GetCities extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final List<String> CITIES = Arrays.asList("Albany", "Albuquerque", "Atlanta", "Austin", "Baltimore",
			"Boston");

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		setResponseHeaders(response);

		String inputCity = request.getParameter("cityInput");
		request.setAttribute("cities", findMatchingCities(inputCity));
		String outputPage = "/WEB-INF/cities-list.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(outputPage);
		dispatcher.include(request, response);
	}

	private List<String> findMatchingCities(String inputCity)
	{
		List<String> matchingCities = new ArrayList<String>();

		String lowerCaseInputCity = inputCity.toLowerCase();

		for (String city : CITIES)
		{
			if (city.toLowerCase().startsWith(lowerCaseInputCity))
			{
				matchingCities.add(city);
			}
		}

		return matchingCities;
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
