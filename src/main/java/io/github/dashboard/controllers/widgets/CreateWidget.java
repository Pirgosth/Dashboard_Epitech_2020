package io.github.dashboard.controllers.widgets;

import com.google.gson.Gson;
import io.github.dashboard.models.widgets.WidgetGridParameters;
import io.github.dashboard.models.widgets.WidgetParameters;
import io.github.dashboard.utils.SqlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/widgets/create"})
public class CreateWidget extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession();
        Boolean isUserConnected = (Boolean) currentSession.getAttribute("isConnected");
        if(isUserConnected == null || !isUserConnected){
            return;
        }
        int userId = (int) currentSession.getAttribute("userId");
        String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson gson = new Gson();
        WidgetParameters parameters = gson.fromJson(data, WidgetParameters.class);
        System.out.println(parameters);
        if(parameters.name == null || parameters.gridParameters == null){
            resp.sendError(204, "Invalid parameters");
            return;
        }

        ResultSet widgetTypeResult;
        int widgetId;
        try {
            widgetTypeResult = SqlUtils.executeSelect("SELECT id FROM widgets WHERE name = ?;", Collections.singletonList(parameters.name));
            if(!widgetTypeResult.first()){
                return;
            }
            widgetId = widgetTypeResult.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        int savedWidgetId;

        try {
            ResultSet insertResult = SqlUtils.executeInsert("INSERT INTO saved_widgets(widget_id, user_id, parameters) VALUES(?, ?, ?);", Arrays.asList(widgetId, userId, gson.toJson(parameters.gridParameters, WidgetGridParameters.class)));
            if(!insertResult.first()){
                return;
            }
            savedWidgetId = insertResult.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        resp.setContentType("application/json");
        resp.getWriter().write(String.format("{\"id\": %s}", savedWidgetId));
    }
}
