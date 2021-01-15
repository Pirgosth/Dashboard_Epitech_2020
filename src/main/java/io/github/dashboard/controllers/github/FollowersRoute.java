package io.github.dashboard.controllers.github;

import com.google.gson.*;
import io.github.dashboard.models.services.github.followers.Follower;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

import static io.github.dashboard.utils.HttpRequestUtils.makeRequest;

@WebServlet(urlPatterns = {"/github/followers"})
public class FollowersRoute extends HttpServlet {

    protected Follower[] getFollowers(String body) {
        Gson gson = new GsonBuilder().create();
        Follower[] followers;
        followers = gson.fromJson(body, Follower[].class);
        return followers;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        Gson gson = new GsonBuilder().create();
        if (username == null) {
            resp.sendError(400);
            return;
        }
        try {
            String body = makeRequest("api.github.com", "/users/" + username + "/followers", Collections.emptyMap());
            Follower[] followers = getFollowers(body);
            resp.setContentType("application/json");
            JsonObject res = new JsonObject();
            res.addProperty("username", username);
            res.add("followers", gson.toJsonTree(followers));
            resp.getWriter().print(gson.toJson(res));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
