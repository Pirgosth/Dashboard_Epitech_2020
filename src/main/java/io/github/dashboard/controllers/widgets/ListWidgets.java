package io.github.dashboard.controllers.widgets;

import com.google.gson.Gson;
import io.github.dashboard.models.widgets.UserWidget;
import io.github.dashboard.utils.WidgetUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/widgets/list"})
public class ListWidgets extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession();

        Boolean isUserConnected = (Boolean) currentSession.getAttribute("isConnected");

        if(isUserConnected == null || !isUserConnected){
            return;
        }

        int userId = (int) currentSession.getAttribute("userId");

        List<UserWidget> userWidgets = WidgetUtils.getUserWidgets(userId);

        Gson gson = new Gson();

        resp.setContentType("application/json");
        resp.getWriter().print(gson.toJson(userWidgets));
    }
}
