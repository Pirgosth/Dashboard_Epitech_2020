package io.github.dashboard.controllers.widgets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.github.dashboard.models.widgets.WidgetGridParameters;
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

@WebServlet(urlPatterns = {"/widgets/update"})
public class UpdateWidget extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession currentSession = req.getSession();
        Boolean isConnected = (Boolean) currentSession.getAttribute("isConnected");
        if (isConnected == null || !isConnected) {
            resp.getWriter().print("{\"updated\": false}");
            return;
        }

        String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        JsonObject obj;

        try {
            obj = JsonParser.parseString(data).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            resp.getWriter().print("{\"updated\": false}");
            return;
        }

        int widgetId;

        int userId = (int) currentSession.getAttribute("userId");
        try {
            widgetId = obj.get("id").getAsInt();
        } catch (ClassCastException | NumberFormatException e) {
            e.printStackTrace();
            resp.getWriter().print("{\"updated\": false}");
            return;
        }

        if (obj.get("parameters") != null) {
            Gson gson = new Gson();
            JsonObject parameters = obj.get("parameters").getAsJsonObject();
            WidgetGridParameters gridParameters;
            try{
                gridParameters = gson.fromJson(parameters, WidgetGridParameters.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                resp.getWriter().print("{\"updated\": false}");
                return;
            }

            try {
                SqlUtils.executeUpdate("UPDATE saved_widgets SET parameters = ? WHERE user_id = ? AND id = ?;", Arrays.asList(gson.toJson(gridParameters, WidgetGridParameters.class), userId, widgetId));
            } catch (SQLException e) {
                e.printStackTrace();
                resp.getWriter().print("{\"updated\": false}");
                return;
            }

        }

        if (obj.get("url_parameters") != null) {
            String url_parameters = obj.get("url_parameters").getAsString();
            try {
                SqlUtils.executeUpdate("UPDATE saved_widgets SET url_parameters = ? WHERE user_id = ? AND id = ?;", Arrays.asList(url_parameters, userId, widgetId));
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(500, "Database write error");
                return;
            }
        }

        resp.getWriter().print("{\"updated\": true}");
    }
}