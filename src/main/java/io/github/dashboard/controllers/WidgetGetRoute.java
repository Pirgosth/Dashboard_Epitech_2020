package io.github.dashboard.controllers;

import io.github.dashboard.utils.SqlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

@WebServlet(urlPatterns = {"/widgets/get_route"})
public class WidgetGetRoute extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String widgetName = req.getParameter("name");
        if(widgetName == null){
            resp.sendError(400);
            return;
        }

        try {
            ResultSet rs = SqlUtils.executeSelect("SELECT route FROM widgets WHERE name = ?;", Collections.singletonList(widgetName));
            if(!rs.first()){
                resp.sendError(204);
                return;
            }

            String route = rs.getString("route");
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().print(String.format("{\"route\": \"%s\"}", route));

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(500, "Database read error");
        }

    }
}
