package io.github.dashboard.controllers.steam;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.dashboard.models.services.steam.SteamGame;
import io.github.dashboard.models.services.steam.SteamInfo;
import io.github.dashboard.models.services.steam.profile.ProfileInfos;
import io.github.dashboard.models.widgets.WidgetCacheResult;
import io.github.dashboard.utils.SteamUtils;
import io.github.dashboard.utils.WidgetUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

import static io.github.dashboard.utils.SteamUtils.getGames;
import static io.github.dashboard.utils.SteamUtils.getProfile;
import static io.github.dashboard.utils.SteamUtils.fromParameterToId;


@WebServlet(urlPatterns = {"/steam/hours"})
public class HoursRoute extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        resp.setContentType("application/json");

        String username = req.getParameter("username");

        JsonObject cacheParameters = new JsonObject();
        cacheParameters.addProperty("username", username);

        Gson gson = new Gson();
        String jsonCacheParameters = gson.toJson(cacheParameters);

        WidgetCacheResult cacheResult = WidgetUtils.getWidgetCachedData("STEAM_HOURS", jsonCacheParameters);

        if(cacheResult != null && cacheResult.foundValid){
            resp.getWriter().print(cacheResult.response);
            return;
        }

        int minutes = 0;
        float hours;
        String profile_name = "";
        try {
            if (username == null) {
                resp.sendError(204);
                return;
            }
            String steamId = fromParameterToId(username);
            if (steamId == null) {
                return;
            }
            ProfileInfos user = getProfile(steamId);
            profile_name = user.personaname;
            SteamInfo infos = getGames(steamId);
            if (infos.games == null) {
                //Empty response
                throw new IllegalStateException("Get Empty Response but expected Json");
            }
            for (SteamGame game : infos.games)
                minutes += game.playtime_forever;
            hours = Math.round((minutes * 10.0) / 60.0) / 10.0f;

        } catch (IllegalStateException | URISyntaxException e) {
            e.printStackTrace();
            hours = -1;
        }

        JsonObject response = new JsonObject();

        response.addProperty("hours", hours);
        response.addProperty("username", profile_name);

        String jsonResponse = gson.toJson(response);
        WidgetUtils.updateWidgetCache("STEAM_HOURS", jsonCacheParameters, jsonResponse, 360000);

        resp.getWriter().printf(jsonResponse);
    }
}
