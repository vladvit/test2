package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = Optional.ofNullable(request.getParameter("action")).orElse("list");
        Integer id = Integer.parseInt(Optional.ofNullable(request.getParameter("userid")).orElse("0"));

        switch (action) {
            case "Login": {
                SecurityUtil.setAuthUserId(id);
                response.sendRedirect("/topjava");
                break;
            }
            default: {
                response.sendRedirect("users");
                break;
            }
        }
    }
}
