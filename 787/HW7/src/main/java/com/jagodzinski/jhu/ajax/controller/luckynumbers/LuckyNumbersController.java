package com.jagodzinski.jhu.ajax.controller.luckynumbers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jagodzinski.jhu.ajax.controller.dataformat.ResponseFormat;
import com.jagodzinski.jhu.ajax.controller.dataformat.ResponseStrategy;
import com.jagodzinski.jhu.ajax.controller.dataformat.ResponseStrategyFactory;

@WebServlet("/LuckyNumbers")
public class LuckyNumbersController extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		int numberOfRandoms = Integer.parseInt(request.getParameter("numberOfRandoms"));
		List<Double> randomNumbers = generateRandomNumbers(numberOfRandoms);

        setNoCacheResponseHeaders(response);

		ResponseStrategy responseStrategy = ResponseStrategyFactory.getResponseStrategy(ResponseFormat.JSON);
		String ajaxResponseBody = responseStrategy.toResponse(randomNumbers);
		response.getWriter().write(ajaxResponseBody);
    }

	private List<Double> generateRandomNumbers(int numberOfRandoms) {
		Double[] randomNumbers = new Double[numberOfRandoms];

		for (int i = 0; i < numberOfRandoms; i++) {
			randomNumbers[i] = Math.random();
		}

		return Arrays.asList(randomNumbers);
	}

	private void setNoCacheResponseHeaders(HttpServletResponse response)
    {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
	}
}
