package io.github.dashboard.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

@WebServlet(urlPatterns = {"/about.json"})
public class About extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        ServletContext servletContext = getServletContext();
        JsonReader reader = new JsonReader(new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/static_about.json")));
        JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();

        String ip = req.getRemoteAddr();
        if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ip = inetAddress.getHostAddress();
        }

        response.get("client").getAsJsonObject().addProperty("host", ip);
        response.get("server").getAsJsonObject().addProperty("current_time", System.currentTimeMillis() / 1000L);

        String jsonResponse = gson.toJson(response);
        resp.setContentType("application/json");
        resp.getWriter().print(jsonResponse);
    }
}
