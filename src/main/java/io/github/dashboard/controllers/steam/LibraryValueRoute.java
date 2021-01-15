package io.github.dashboard.controllers.steam;

import com.google.gson.*;
import io.github.dashboard.models.services.steam.SteamGame;
import io.github.dashboard.models.services.steam.SteamInfo;
import io.github.dashboard.models.services.steam.libraryValue.Price;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.dashboard.utils.HttpRequestUtils.makeRequest;
import static io.github.dashboard.utils.SteamUtils.*;

@WebServlet(urlPatterns = {"/steam/value"})
public class LibraryValueRoute extends HttpServlet {

    public static List<Price> getGamesPrice(String body) {
        JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
        Gson gson = new GsonBuilder().create();

        List<Price> prices = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            JsonElement elem = entry.getValue();
            Price price = new Price();
            JsonElement elemData = elem.getAsJsonObject().get("data");
            if (elemData != null && elemData.isJsonObject()) {
                JsonObject price_overview = elemData.getAsJsonObject().get("price_overview").getAsJsonObject();
                if (price_overview != null)
                    price = gson.fromJson(price_overview, Price.class);
            }
            prices.add(price);
        }
        return prices;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        String username = req.getParameter("username");

        JsonObject cacheParameters = new JsonObject();
        cacheParameters.addProperty("username", username);

        Gson gson = new Gson();
        String jsonCacheParameters = gson.toJson(cacheParameters);

        WidgetCacheResult cacheResult = WidgetUtils.getWidgetCachedData("STEAM_VALUE", jsonCacheParameters);

        if (cacheResult != null && cacheResult.foundValid) {
            resp.getWriter().print(cacheResult.response);
            return;
        }
        if (username == null) {
            resp.sendError(204);
            return;
        }
        String steamId = fromParameterToId(username);
        if (steamId == null) {
            return;
        }
        try {
            SteamInfo info = getGames(steamId);
            StringBuilder appids = new StringBuilder();
            float cost = 0f;
            if (info.games == null) {
                cost = -1;
            } else {
                for (SteamGame game : info.games) {
                    appids.append(game.appid).append(",");
                }
                appids = new StringBuilder(appids.substring(0, appids.length() - 1));
                Map<String, String> tmpParameters = new HashMap<>();
                tmpParameters.put("appids", appids.toString());
                tmpParameters.put("cc", "fr");
                tmpParameters.put("filters", "price_overview");
                String appsBody = makeRequest("store.steampowered.com", "/api/appdetails", tmpParameters);
                List<Price> prices = getGamesPrice(appsBody);

                for (Price price : prices)
                    cost += price.initial / 100.0f;
                cost = Math.round(cost * 100.0) / 100.0f;
            }

            ProfileInfos user = getProfile(steamId);

            JsonObject response = new JsonObject();

            response.addProperty("cost", cost);
            response.addProperty("username", user.personaname);

            String jsonResponse = gson.toJson(response);
            WidgetUtils.updateWidgetCache("STEAM_VALUE", jsonCacheParameters, jsonResponse, 180000);

            resp.getWriter().printf(jsonResponse);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
