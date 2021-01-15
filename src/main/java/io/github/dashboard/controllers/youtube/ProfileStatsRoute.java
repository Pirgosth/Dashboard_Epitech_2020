package io.github.dashboard.controllers.youtube;

import com.google.gson.*;
import io.github.dashboard.models.services.youtube.ChannelInfos;
import io.github.dashboard.models.services.youtube.ProfileStats.UserStats;
import io.github.dashboard.models.widgets.WidgetCacheResult;
import io.github.dashboard.utils.WidgetUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static io.github.dashboard.utils.YoutubeUtils.getChannelInfos;
import static io.github.dashboard.utils.HttpRequestUtils.makeRequest;

@WebServlet(urlPatterns = {"/youtube/profile"})
public class ProfileStatsRoute extends HttpServlet {
    //Set your Youtube API key here
    private final static String API_KEY = "";

    protected UserStats getUserStats(String id) throws IOException, URISyntaxException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("part", "statistics");
        parameters.put("key", API_KEY);
        parameters.put("id", id);

        String body = makeRequest("www.googleapis.com", "/youtube/v3/channels", parameters);
        JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(obj.get("items").getAsJsonArray().get(0).getAsJsonObject().get("statistics").getAsJsonObject(), UserStats.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String channelParameter = req.getParameter("channelParameter");
        if (channelParameter == null) {
            resp.sendError(400);
            return;
        }

        JsonObject cacheParameters = new JsonObject();
        cacheParameters.addProperty("channelParameter", channelParameter);

        Gson gson = new Gson();
        String jsonCacheParameters = gson.toJson(cacheParameters);

        WidgetCacheResult cacheResult = WidgetUtils.getWidgetCachedData("YOUTUBE_CHANNEL_STATS", jsonCacheParameters);
        if(cacheResult != null && cacheResult.foundValid){
            resp.getWriter().print(cacheResult.response);
            return;
        }

        try {
            ChannelInfos channelInfos = getChannelInfos(channelParameter);
            UserStats userStats = getUserStats(channelInfos.id);
            JsonObject res = new JsonObject();
            res.addProperty("channelName", channelInfos.snippet.title);
            res.add("thumbnails", gson.toJsonTree(channelInfos.snippet.thumbnails));
            res.add("statistics", gson.toJsonTree(userStats));

            String jsonResponse = gson.toJson(res);
            System.out.println(jsonResponse);

            WidgetUtils.updateWidgetCache("YOUTUBE_CHANNEL_STATS", jsonCacheParameters, jsonResponse, 30*60*1000);

            resp.getWriter().print(jsonResponse);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
