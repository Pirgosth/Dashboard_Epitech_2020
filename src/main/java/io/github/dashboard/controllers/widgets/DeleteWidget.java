package io.github.dashboard.controllers.widgets;

import com.google.gson.JsonParser;
import io.github.dashboard.utils.SqlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/widgets/delete"})
public class DeleteWidget extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession();

        Boolean isConnected = (Boolean) currentSession.getAttribute("isConnected");
        if(isConnected == null || !isConnected){
            return;
        }

        int userId = (int) currentSession.getAttribute("userId");

        String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        String widgetIdRaw = JsonParser.parseString(data).getAsJsonObject().get("id").getAsString();

        if(widgetIdRaw == null){
            resp.sendError(204);
            return;
        }
        int widgetId;
        try{
            widgetId = Integer.parseInt(widgetIdRaw);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendError(204);
            return;
        }

        try {
            SqlUtils.executeDelete("DELETE FROM saved_widgets WHERE user_id = ? AND id = ?;", Arrays.asList(userId, widgetId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.sendError(200);
    }
}
