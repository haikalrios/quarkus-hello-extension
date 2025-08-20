package com.haikal.hello.ext.runtime;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet
public class HelloServlet extends HttpServlet {
    private String message;
    private String context;

    @Override
    public void init() {
        this.message = getServletConfig().getInitParameter("message");
        this.context = getServletConfig().getInitParameter("context"); // pode ser null
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String out = message != null ? message : "Hello from Quarkus extension!";
        if (context != null && !context.isEmpty()) {
            out = out + " | context=" + context;
        }

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        try {
            resp.getWriter().write(out);
        } catch (IOException e) {
            // Log the exception or handle it appropriately
            // For example, you could set an error status code
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
