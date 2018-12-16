package Servlets;

import data.Repositories;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        JSONObject message = new JSONObject();

        if (Repositories.getMysqlBreakoutRepository().login(name, password)) {
            message.put("message", "success");
            message.put("sessionId", request.getSession().getId());
            request.getSession().setAttribute("username", name);
        } else {
            message.put("message", "failure");
        }

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.print(message);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
