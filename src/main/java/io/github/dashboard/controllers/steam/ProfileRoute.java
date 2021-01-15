package io.github.dashboard.controllers.steam;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.dashboard.models.services.steam.profile.ProfileInfos;
import io.github.dashboard.models.widgets.WidgetCacheResult;
import io.github.dashboard.utils.SteamUtils;
import io.github.dashboard.utils.WidgetUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

import static io.github.dashboard.utils.SteamUtils.*;

@WebServlet(urlPatterns = {"/steam/profile"})
public class ProfileRoute extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        String username = req.getParameter("username");

        Gson gson = new Gson();
        JsonObject cacheParameters = new JsonObject();
        cacheParameters.addProperty("username", username);
        String jsonCacheParameters = gson.toJson(cacheParameters);

        WidgetCacheResult cacheResult = WidgetUtils.getWidgetCachedData("STEAM_PROFILE", jsonCacheParameters);
        if(cacheResult != null && cacheResult.foundValid){
            resp.getWriter().print(cacheResult.response);
            return;
        }
        String steamId = fromParameterToId(username);
        if (steamId == null)
            return;
        ProfileInfos infos;
        try {
            infos = getProfile(steamId);
            int level = getPlayerLevel(steamId);
            int friends = getNbrFriends(steamId);
            JsonObject res = JsonParser.parseString(gson.toJson(infos, ProfileInfos.class)).getAsJsonObject();
            res.addProperty("level", level);
            res.addProperty("friends", friends);
            String jsonResponse = gson.toJson(res);

            WidgetUtils.updateWidgetCache("STEAM_PROFILE", jsonCacheParameters, jsonResponse, 60*60*1000);

            resp.getWriter().print(jsonResponse);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
