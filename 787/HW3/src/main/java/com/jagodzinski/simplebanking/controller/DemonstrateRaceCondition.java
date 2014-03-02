package com.jagodzinski.simplebanking.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@WebServlet("/DemonstrateRaceCondition")
public class DemonstrateRaceCondition extends HttpServlet {

    private static AtomicLong requestCount = new AtomicLong();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        simulateLongRunningQuery();
        response.getWriter().write("This is request number " + requestCount.incrementAndGet());
    }

    private void simulateLongRunningQuery() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
