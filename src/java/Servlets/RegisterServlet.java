package Servlets;

import data.Repositories;
import data.util.FormValidation;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        FormValidation formValidator = new FormValidation();

        JSONObject message = formValidator.formValidationRegistration(email, name, password);

        if (message.toString().equals("{\"message\":\"success\"}")) {
            if (!Repositories.getMysqlBreakoutRepository().register(email, name, password)) {
                message.put("message", "duplicate");
            } else {
                request.getSession().setAttribute("username", name);
            }
        }

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.println(message);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
