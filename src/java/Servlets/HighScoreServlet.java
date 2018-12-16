package Servlets;

import data.Repositories;
import org.json.simple.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

@WebServlet(name = "HighScoreServlet", urlPatterns = {"/highScore"})
public class HighScoreServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        JSONArray highscores = Repositories.getMysqlBreakoutRepository().getHighscores();
        PrintWriter out = response.getWriter();
        System.out.println(highscores);
        out.print(highscores);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        int score = Integer.parseInt(req.getParameter("score"));
        String type = req.getParameter("type");
        Date date = Calendar.getInstance().getTime();
        Repositories.getMysqlBreakoutRepository().setHighscore(name, type, score, date);
    }
}
